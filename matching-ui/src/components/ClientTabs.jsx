import React, { useState } from 'react';
import '../styles/ClientTabs.css';
import ApartmentsTab from './ApartmentsTab';
import OrdersTab from './OrdersTab';
import TradesTab from './TradesTab';

export default function ClientTabs({ client }) {
  const [activeTab, setActiveTab] = useState('apartments');

  const renderContent = () => {
    switch (activeTab) {
      case 'apartments':
        return <ApartmentsTab client={client} />;
      case 'orders':
        return <OrdersTab client={client} />;
      case 'trades':
        return <TradesTab client={client} />;
      default:
        return null;
    }
  };

  return (
    <div className="client-tabs">
      <div className="tab-buttons">
        <button onClick={() => setActiveTab('apartments')} className={activeTab === 'apartments' ? 'active' : ''}>Мои квартиры</button>
        <button onClick={() => setActiveTab('orders')} className={activeTab === 'orders' ? 'active' : ''}>Мои ордеры</button>
        <button onClick={() => setActiveTab('trades')} className={activeTab === 'trades' ? 'active' : ''}>Мои трейды</button>
      </div>
      <div className="tab-content">{renderContent()}</div>
    </div>
  );
}
