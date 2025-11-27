import React from 'react';
import logo from '../img/logo.svg';

export default function Header({ title, description }) {
  return (
    <div className="rounded-t-3xl shadow-lg p-8 text-white" style={{background: 'linear-gradient(to right, #2563EB 50%, #60A5FA 100%)'}}>
      <div className="flex items-center mb-4">
        <img src={logo} alt="E-TECH STORE" className="h-12" />
      </div>
      {title && <h1 className="text-3xl font-bold text-center">{title}</h1>}
      {description && (
        <p className="text-center mt-2 text-blue-100">{description}</p>
      )}
    </div>
  );
}
