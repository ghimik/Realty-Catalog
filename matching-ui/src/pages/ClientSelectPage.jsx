import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ClientCard from '../components/ClientCard';
import { fetchClients } from '../api';
import '../styles/ClientSelectPage.css';

export default function ClientSelectPage({ setSelectedClient }) {
  const [clients, setClients] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    async function loadClients() {
      const data = await fetchClients();
      setClients(data);
    }
    loadClients();
  }, []);

  const handleSelect = (client) => {
    setSelectedClient(client);
    navigate('/client');
  };

  return (
    <div className="client-select-page">
      <h1>Выберите клиента</h1>
      <div className="client-list">
        {clients.map(client => (
          <ClientCard key={client.id} client={client} onSelect={handleSelect} />
        ))}
        {clients.length === 0 && <p>Клиенты не найдены...</p>}
      </div>
    </div>
  );
}
