import React from 'react';
import '../styles/ClientInfo.css';

export default function ClientInfo({ client }) {
  return (
    <div className="client-info">
      <h2>{client.name}</h2>
      <p>Телефон: {client.phone || 'не указан'}</p>
      <p>Email: {client.email || 'не указан'}</p>
    </div>
  );
}
