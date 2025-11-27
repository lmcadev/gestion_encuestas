import React, { useEffect, useState } from 'react';

export default function Preguntas() {
  const [preguntas, setPreguntas] = useState([]);
  const [encuestas, setEncuestas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingPregunta, setEditingPregunta] = useState(null);
  const [formData, setFormData] = useState({
    texto: '',
    tipoPregunta: 'ESCALA',
    obligatoria: true,
    orden: 1,
    encuestaId: ''
  });

  useEffect(() => {
    fetchPreguntas();
    fetchEncuestas();
  }, []);

  const fetchPreguntas = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/preguntas', {
        headers: { Authorization: `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setPreguntas(data);
      }
      setLoading(false);
    } catch (error) {
      console.error('Error al cargar preguntas:', error);
      setLoading(false);
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
    } catch (error) {
      console.error('Error al cargar encuestas:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      const url = editingPregunta ? `/api/preguntas/${editingPregunta.id}` : '/api/preguntas';
      const method = editingPregunta ? 'PUT' : 'POST';

      const payload = {
        texto: formData.texto,
        tipoPregunta: formData.tipoPregunta,
        obligatoria: formData.obligatoria,
        orden: parseInt(formData.orden),
        encuestaId: formData.encuestaId
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
        fetchPreguntas();
        closeModal();
      } else {
        const error = await response.text();
        alert('Error al guardar: ' + error);
      }
    } catch (error) {
      console.error('Error al guardar pregunta:', error);
      alert('Error al guardar la pregunta');
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('¿Estás seguro de eliminar esta pregunta?')) return;
    
    try {
      const token = localStorage.getItem('token');
      const response = await fetch(`/api/preguntas/${id}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` }
      });
      if (response.ok) {
        fetchPreguntas();
      }
    } catch (error) {
      console.error('Error al eliminar pregunta:', error);
    }
  };

  const openModal = (pregunta = null) => {
    if (pregunta) {
      setEditingPregunta(pregunta);
      setFormData({
        texto: pregunta.texto,
        tipoPregunta: pregunta.tipoPregunta,
        obligatoria: pregunta.obligatoria,
        orden: pregunta.orden,
        encuestaId: pregunta.encuesta?.id || ''
      });
    } else {
      setEditingPregunta(null);
      setFormData({
        texto: '',
        tipoPregunta: 'ESCALA',
        obligatoria: true,
        orden: 1,
        encuestaId: ''
      });
    }
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setEditingPregunta(null);
    setFormData({
      texto: '',
      tipoPregunta: 'ESCALA',
      obligatoria: true,
      orden: 1,
      encuestaId: ''
    });
  };

  const getTipoPreguntaLabel = (tipo) => {
    const tipos = {
      'ESCALA': 'Escala (1-5)',
      'TEXTO': 'Texto libre',
      'SI_NO': 'Sí/No',
      'MULTIPLE': 'Opción múltiple'
    };
    return tipos[tipo] || tipo;
  };

  if (loading) {
    return <div className="flex items-center justify-center h-full">Cargando...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-800">Preguntas</h1>
        <button
          onClick={() => openModal()}
          className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg flex items-center gap-2"
        >
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Nueva Pregunta
        </button>
      </div>

      {/* Listado de preguntas */}
      <div className="bg-white rounded-xl shadow-md border border-gray-200 overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Orden
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Pregunta
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tipo
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Encuesta
              </th>
              <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                Obligatoria
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Acciones
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {preguntas.map((pregunta) => (
              <tr key={pregunta.id} className="hover:bg-gray-50 transition-colors">
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="flex items-center justify-center w-8 h-8 bg-blue-100 text-blue-800 rounded-full font-semibold text-sm">
                    {pregunta.orden}
                  </div>
                </td>
                <td className="px-6 py-4">
                  <div className="text-sm text-gray-900">{pregunta.texto}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className="px-2 py-1 text-xs font-semibold rounded-full bg-purple-100 text-purple-800">
                    {getTipoPreguntaLabel(pregunta.tipoPregunta)}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm text-gray-900">{pregunta.encuesta?.titulo || 'Sin encuesta'}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-center">
                  {pregunta.obligatoria ? (
                    <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-semibold bg-green-100 text-green-800">
                      Sí
                    </span>
                  ) : (
                    <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-semibold bg-gray-100 text-gray-800">
                      No
                    </span>
                  )}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <button
                    onClick={() => openModal(pregunta)}
                    className="text-blue-600 hover:text-blue-900 mr-4"
                  >
                    Editar
                  </button>
                  <button
                    onClick={() => handleDelete(pregunta.id)}
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

      {preguntas.length === 0 && (
        <div className="text-center py-12">
          <svg className="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <p className="text-gray-500">No hay preguntas registradas</p>
        </div>
      )}

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl shadow-2xl p-8 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">
              {editingPregunta ? 'Editar Pregunta' : 'Nueva Pregunta'}
            </h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Encuesta *
                </label>
                <select
                  value={formData.encuestaId}
                  onChange={(e) => setFormData({ ...formData, encuestaId: e.target.value })}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="">Selecciona una encuesta</option>
                  {encuestas.map((encuesta) => (
                    <option key={encuesta.id} value={encuesta.id}>
                      {encuesta.titulo} - {encuesta.producto?.nombre}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Texto de la Pregunta *
                </label>
                <textarea
                  value={formData.texto}
                  onChange={(e) => setFormData({ ...formData, texto: e.target.value })}
                  required
                  placeholder="Ej: ¿Qué tan satisfecho estás con el producto?"
                  rows={3}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Tipo de Pregunta *
                  </label>
                  <select
                    value={formData.tipoPregunta}
                    onChange={(e) => setFormData({ ...formData, tipoPregunta: e.target.value })}
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  >
                    <option value="ESCALA">Escala (1-5)</option>
                    <option value="TEXTO">Texto libre</option>
                    <option value="SI_NO">Sí/No</option>
                    <option value="MULTIPLE">Opción múltiple</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Orden *
                  </label>
                  <input
                    type="number"
                    value={formData.orden}
                    onChange={(e) => setFormData({ ...formData, orden: e.target.value })}
                    required
                    min="1"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
              </div>

              <div>
                <label className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={formData.obligatoria}
                    onChange={(e) => setFormData({ ...formData, obligatoria: e.target.checked })}
                    className="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
                  />
                  <span className="text-sm font-medium text-gray-700">
                    Pregunta obligatoria
                  </span>
                </label>
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
                  {editingPregunta ? 'Actualizar' : 'Crear'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
