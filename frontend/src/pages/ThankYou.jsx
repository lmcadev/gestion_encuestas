import React from 'react';
import { Link } from 'react-router-dom';

export default function ThankYou() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-600 to-blue-800 flex items-center justify-center px-4">
      <div className="bg-white rounded-3xl shadow-2xl p-12 max-w-2xl text-center">
        <div className="mb-8">
          <div className="inline-block bg-green-100 p-6 rounded-full">
            <svg
              className="w-20 h-20 text-green-600"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M5 13l4 4L19 7"
              />
            </svg>
          </div>
        </div>

        <h1 className="text-4xl font-bold text-gray-800 mb-4">
          ¡Gracias por tu tiempo!
        </h1>
        
        <p className="text-xl text-gray-600 mb-8">
          Tu opinión es muy importante para nosotros y nos ayuda a mejorar nuestros productos y servicios.
        </p>

        <div className="bg-blue-50 rounded-xl p-6 mb-8">
          <p className="text-gray-700">
            Hemos recibido tus respuestas exitosamente. Nuestro equipo revisará tus comentarios para continuar ofreciéndote la mejor experiencia posible.
          </p>
        </div>

        <Link
          to="/"
          className="inline-block bg-blue-600 hover:bg-blue-700 text-white font-bold py-4 px-8 rounded-full transition-all transform hover:scale-105"
        >
          Volver al inicio
        </Link>
      </div>
    </div>
  );
}
