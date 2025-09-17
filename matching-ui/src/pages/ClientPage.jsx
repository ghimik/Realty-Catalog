import React from 'react';
import ClientInfo from '../components/ClientInfo';
import ClientTabs from '../components/ClientTabs';
import '../styles/ClientPage.css';

export default function ClientPage({ client }) {
  return (
    <div className="client-page">
      <ClientInfo client={client} />
      <ClientTabs client={client}/>
    </div>
  );
}
