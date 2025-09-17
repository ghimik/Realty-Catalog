import React from 'react';
import '../styles/ClientCard.css';

export default function ClientCard({ client, onSelect }) {
  return (
    <div className="client-card" onClick={() => onSelect(client)}>
      <h3>{client.name}</h3>
      <button onClick={() => onSelect(client)}>Выбрать</button>
    </div>
  );
}
