import React, { useEffect, useState } from 'react';

export default function Encuestas() {
  const [encuestas, setEncuestas] = useState([]);
  const [productos, setProductos] = useState([]);
  const [clientes, setClientes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingEncuesta, setEditingEncuesta] = useState(null);
  const [showClienteModal, setShowClienteModal] = useState(false);
  const [currentEncuesta, setCurrentEncuesta] = useState(null);
  const [sendMethod, setSendMethod] = useState(''); // 'whatsapp' o 'email'
  const [selectedCliente, setSelectedCliente] = useState('');
  const [newCliente, setNewCliente] = useState({
    nombre: '',
    email: '',
    telefono: '',
    canalOrigen: 'Web'
  });
  const [formData, setFormData] = useState({
    titulo: '',
    descripcion: '',
    productoId: '',
    fechaInicio: new Date().toISOString().split('T')[0]
  });

  useEffect(() => {
    fetchEncuestas();
    fetchProductos();
    fetchClientes();
  }, []);

  const fetchClientes = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/clientes', {
        headers: { Authorization: `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setClientes(data);
      }
    } catch (error) {
      console.error('Error al cargar clientes:', error);
    }
  };

  const fetchProductos = async () => {
    try {
      const response = await fetch('/api/productos');
      if (response.ok) {
        const data = await response.json();
        setProductos(data);
      }
    } catch (error) {
      console.error('Error al cargar productos:', error);
    }
  };

  const fetchEncuestas = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/encuestas', {
        headers: { Authorization: `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setEncuestas(data);
      }
      setLoading(false);
    } catch (error) {
      console.error('Error al cargar encuestas:', error);
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      const url = editingEncuesta ? `/api/encuestas/${editingEncuesta.id}` : '/api/encuestas';
      const method = editingEncuesta ? 'PUT' : 'POST';

      const payload = {
        titulo: formData.titulo,
        descripcion: formData.descripcion,
        productoId: formData.productoId,
        fechaInicio: formData.fechaInicio
      };

      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        fetchEncuestas();
        closeModal();
      } else {
        const error = await response.text();
        alert('Error al guardar: ' + error);
      }
    } catch (error) {
      console.error('Error al guardar encuesta:', error);
      alert('Error al guardar la encuesta');
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('¿Estás seguro de eliminar esta encuesta?')) return;
    
    try {
      const token = localStorage.getItem('token');
      const response = await fetch(`/api/encuestas/${id}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` }
      });
      if (response.ok) {
        fetchEncuestas();
      }
    } catch (error) {
      console.error('Error al eliminar encuesta:', error);
    }
  };

  const copyEncuestaLink = (encuestaId) => {
    const link = `${window.location.origin}/encuesta/${encuestaId}`;
    navigator.clipboard.writeText(link);
    alert('Enlace copiado al portapapeles');
  };

  const enviarPorWhatsApp = (encuestaId, titulo) => {
    setCurrentEncuesta({ id: encuestaId, titulo });
    setSendMethod('whatsapp');
    setShowClienteModal(true);
  };

  const enviarPorCorreo = (encuestaId, titulo) => {
    setCurrentEncuesta({ id: encuestaId, titulo });
    setSendMethod('email');
    setShowClienteModal(true);
  };

  const handleEnviarACliente = async () => {
    let clienteId = selectedCliente;

    // Si se seleccionó "nuevo", crear el cliente primero
    if (selectedCliente === 'nuevo') {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch('/api/clientes', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`
          },
          body: JSON.stringify(newCliente)
        });

        if (response.ok) {
          const createdCliente = await response.json();
          clienteId = createdCliente.id;
          fetchClientes(); // Actualizar lista de clientes
        } else {
          alert('Error al crear el cliente');
          return;
        }
      } catch (error) {
        console.error('Error al crear cliente:', error);
        alert('Error al crear el cliente');
        return;
      }
    }

    // Buscar datos del cliente
    const cliente = clientes.find(c => c.id === clienteId);
    if (!cliente && clienteId !== selectedCliente) {
      // Si acabamos de crear el cliente, usar los datos del formulario
      const link = `${window.location.origin}/encuesta/${currentEncuesta.id}`;
      
      if (sendMethod === 'whatsapp') {
        const mensaje = `Hola ${newCliente.nombre}, te invito a completar esta encuesta: ${currentEncuesta.titulo}\n\n${link}`;
        const whatsappUrl = newCliente.telefono 
          ? `https://wa.me/${newCliente.telefono.replace(/\D/g, '')}?text=${encodeURIComponent(mensaje)}`
          : `https://wa.me/?text=${encodeURIComponent(mensaje)}`;
        window.open(whatsappUrl, '_blank');
      } else if (sendMethod === 'email') {
        const asunto = `Invitación a encuesta: ${currentEncuesta.titulo}`;
        const cuerpo = `Hola ${newCliente.nombre},\n\nTe invito a completar esta encuesta: ${currentEncuesta.titulo}\n\nPuedes acceder aquí: ${link}\n\n¡Gracias por tu participación!`;
        const mailtoUrl = `mailto:${newCliente.email}?subject=${encodeURIComponent(asunto)}&body=${encodeURIComponent(cuerpo)}`;
        window.location.href = mailtoUrl;
      }
    } else if (cliente) {
      const link = `${window.location.origin}/encuesta/${currentEncuesta.id}`;
      
      if (sendMethod === 'whatsapp') {
        const mensaje = `Hola ${cliente.nombre}, te invito a completar esta encuesta: ${currentEncuesta.titulo}\n\n${link}`;
        const whatsappUrl = cliente.telefono 
          ? `https://wa.me/${cliente.telefono.replace(/\D/g, '')}?text=${encodeURIComponent(mensaje)}`
          : `https://wa.me/?text=${encodeURIComponent(mensaje)}`;
        window.open(whatsappUrl, '_blank');
      } else if (sendMethod === 'email') {
        const asunto = `Invitación a encuesta: ${currentEncuesta.titulo}`;
        const cuerpo = `Hola ${cliente.nombre},\n\nTe invito a completar esta encuesta: ${currentEncuesta.titulo}\n\nPuedes acceder aquí: ${link}\n\n¡Gracias por tu participación!`;
        const mailtoUrl = `mailto:${cliente.email}?subject=${encodeURIComponent(asunto)}&body=${encodeURIComponent(cuerpo)}`;
        window.location.href = mailtoUrl;
      }
    }

    closeClienteModal();
  };

  const closeClienteModal = () => {
    setShowClienteModal(false);
    setCurrentEncuesta(null);
    setSendMethod('');
    setSelectedCliente('');
    setNewCliente({
      nombre: '',
      email: '',
      telefono: '',
      canalOrigen: 'Web'
    });
  };

  const openModal = (encuesta = null) => {
    if (encuesta) {
      setEditingEncuesta(encuesta);
      setFormData({
        titulo: encuesta.titulo,
        descripcion: encuesta.descripcion,
        productoId: encuesta.producto?.id || '',
        fechaInicio: encuesta.fechaInicio ? encuesta.fechaInicio.split('T')[0] : new Date().toISOString().split('T')[0]
      });
    } else {
      setEditingEncuesta(null);
      setFormData({
        titulo: '',
        descripcion: '',
        productoId: '',
        fechaInicio: new Date().toISOString().split('T')[0]
      });
    }
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setEditingEncuesta(null);
    setFormData({
      titulo: '',
      descripcion: '',
      productoId: '',
      fechaInicio: new Date().toISOString().split('T')[0]
    });
  };

  if (loading) {
    return <div className="flex items-center justify-center h-full">Cargando...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-800">Encuestas</h1>
        <button
          onClick={() => openModal()}
          className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg flex items-center gap-2"
        >
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Nueva Encuesta
        </button>
      </div>

      {/* Lista de encuestas */}
      <div className="bg-white rounded-xl shadow-md border border-gray-200 overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Encuesta
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Producto
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Fecha Inicio
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Acciones
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {encuestas.map((encuesta) => (
              <tr key={encuesta.id} className="hover:bg-gray-50 transition-colors">
                <td className="px-6 py-4">
                  <div className="text-sm font-medium text-gray-900">{encuesta.titulo}</div>
                  <div className="text-sm text-gray-500">{encuesta.descripcion}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm text-gray-900">{encuesta.producto?.nombre || 'Sin producto'}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm text-gray-900">
                    {encuesta.fechaInicio ? new Date(encuesta.fechaInicio).toLocaleDateString('es-ES') : '-'}
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <button
                    onClick={() => copyEncuestaLink(encuesta.id)}
                    className="text-green-600 hover:text-green-900 mr-3"
                    title="Copiar URL"
                  >
                    <svg className="w-5 h-5 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                    </svg>
                  </button>
                  <button
                    onClick={() => enviarPorWhatsApp(encuesta.id, encuesta.titulo)}
                    className="text-green-600 hover:text-green-900 mr-3"
                    title="Enviar por WhatsApp"
                  >
                    <svg className="w-5 h-5 inline" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M17.472 14.382c-.297-.149-1.758-.867-2.03-.967-.273-.099-.471-.148-.67.15-.197.297-.767.966-.94 1.164-.173.199-.347.223-.644.075-.297-.15-1.255-.463-2.39-1.475-.883-.788-1.48-1.761-1.653-2.059-.173-.297-.018-.458.13-.606.134-.133.298-.347.446-.52.149-.174.198-.298.298-.497.099-.198.05-.371-.025-.52-.075-.149-.669-1.612-.916-2.207-.242-.579-.487-.5-.669-.51-.173-.008-.371-.01-.57-.01-.198 0-.52.074-.792.372-.272.297-1.04 1.016-1.04 2.479 0 1.462 1.065 2.875 1.213 3.074.149.198 2.096 3.2 5.077 4.487.709.306 1.262.489 1.694.625.712.227 1.36.195 1.871.118.571-.085 1.758-.719 2.006-1.413.248-.694.248-1.289.173-1.413-.074-.124-.272-.198-.57-.347m-5.421 7.403h-.004a9.87 9.87 0 01-5.031-1.378l-.361-.214-3.741.982.998-3.648-.235-.374a9.86 9.86 0 01-1.51-5.26c.001-5.45 4.436-9.884 9.888-9.884 2.64 0 5.122 1.03 6.988 2.898a9.825 9.825 0 012.893 6.994c-.003 5.45-4.437 9.884-9.885 9.884m8.413-18.297A11.815 11.815 0 0012.05 0C5.495 0 .16 5.335.157 11.892c0 2.096.547 4.142 1.588 5.945L.057 24l6.305-1.654a11.882 11.882 0 005.683 1.448h.005c6.554 0 11.89-5.335 11.893-11.893a11.821 11.821 0 00-3.48-8.413Z"/>
                    </svg>
                  </button>
                  <button
                    onClick={() => enviarPorCorreo(encuesta.id, encuesta.titulo)}
                    className="text-blue-600 hover:text-blue-900 mr-3"
                    title="Enviar por Correo"
                  >
                    <svg className="w-5 h-5 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                    </svg>
                  </button>
                  <button
                    onClick={() => openModal(encuesta)}
                    className="text-gray-600 hover:text-gray-900 mr-3"
                  >
                    Editar
                  </button>
                  <button
                    onClick={() => handleDelete(encuesta.id)}
                    className="text-red-600 hover:text-red-900"
                  >
                    Eliminar
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {encuestas.length === 0 && (
        <div className="text-center py-12">
          <svg className="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
          <p className="text-gray-500">No hay encuestas registradas</p>
        </div>
      )}

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl shadow-2xl p-8 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">
              {editingEncuesta ? 'Editar Encuesta' : 'Nueva Encuesta'}
            </h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Título de la Encuesta *
                </label>
                <input
                  type="text"
                  value={formData.titulo}
                  onChange={(e) => setFormData({ ...formData, titulo: e.target.value })}
                  required
                  placeholder="Ej: Encuesta de Satisfacción"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Descripción
                </label>
                <textarea
                  value={formData.descripcion}
                  onChange={(e) => setFormData({ ...formData, descripcion: e.target.value })}
                  placeholder="Descripción opcional de la encuesta"
                  rows={3}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Producto *
                </label>
                <select
                  value={formData.productoId}
                  onChange={(e) => setFormData({ ...formData, productoId: e.target.value })}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="">Selecciona un producto</option>
                  {productos.map((producto) => (
                    <option key={producto.id} value={producto.id}>
                      {producto.nombre}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Fecha de Inicio *
                </label>
                <input
                  type="date"
                  value={formData.fechaInicio}
                  onChange={(e) => setFormData({ ...formData, fechaInicio: e.target.value })}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              <div className="flex gap-4 mt-6">
                <button
                  type="button"
                  onClick={closeModal}
                  className="flex-1 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className="flex-1 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg"
                >
                  {editingEncuesta ? 'Actualizar' : 'Crear'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal de Selección de Cliente */}
      {showClienteModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl shadow-2xl p-6 max-w-md w-full mx-4">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-gray-800">
                Seleccionar Cliente
              </h2>
              <button
                onClick={closeClienteModal}
                className="text-gray-500 hover:text-gray-700"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Selecciona un cliente o crea uno nuevo
                </label>
                <select
                  value={selectedCliente}
                  onChange={(e) => setSelectedCliente(e.target.value)}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="">-- Seleccionar --</option>
                  {clientes.map((cliente) => (
                    <option key={cliente.id} value={cliente.id}>
                      {cliente.nombre} - {cliente.email}
                    </option>
                  ))}
                  <option value="nuevo">+ Crear Nuevo Cliente</option>
                </select>
              </div>

              {/* Formulario de nuevo cliente */}
              {selectedCliente === 'nuevo' && (
                <div className="space-y-3 border-t pt-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Nombre *
                    </label>
                    <input
                      type="text"
                      required
                      value={newCliente.nombre}
                      onChange={(e) => setNewCliente({ ...newCliente, nombre: e.target.value })}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      placeholder="Nombre completo"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Email *
                    </label>
                    <input
                      type="email"
                      required
                      value={newCliente.email}
                      onChange={(e) => setNewCliente({ ...newCliente, email: e.target.value })}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      placeholder="correo@ejemplo.com"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Teléfono {sendMethod === 'whatsapp' && '*'}
                    </label>
                    <input
                      type="tel"
                      required={sendMethod === 'whatsapp'}
                      value={newCliente.telefono}
                      onChange={(e) => setNewCliente({ ...newCliente, telefono: e.target.value })}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      placeholder="+1 234 567 8900"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Canal de Origen
                    </label>
                    <select
                      value={newCliente.canalOrigen}
                      onChange={(e) => setNewCliente({ ...newCliente, canalOrigen: e.target.value })}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    >
                      <option value="Web">Web</option>
                      <option value="WhatsApp">WhatsApp</option>
                      <option value="Email">Email</option>
                      <option value="Tienda">Tienda</option>
                      <option value="Teléfono">Teléfono</option>
                      <option value="Redes Sociales">Redes Sociales</option>
                    </select>
                  </div>
                </div>
              )}

              <div className="flex gap-3 pt-4">
                <button
                  type="button"
                  onClick={closeClienteModal}
                  className="flex-1 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
                >
                  Cancelar
                </button>
                <button
                  type="button"
                  onClick={handleEnviarACliente}
                  disabled={!selectedCliente || (selectedCliente === 'nuevo' && (!newCliente.nombre || !newCliente.email || (sendMethod === 'whatsapp' && !newCliente.telefono)))}
                  className="flex-1 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg disabled:bg-gray-300 disabled:cursor-not-allowed"
                >
                  {sendMethod === 'whatsapp' ? 'Enviar WhatsApp' : 'Enviar Email'}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
