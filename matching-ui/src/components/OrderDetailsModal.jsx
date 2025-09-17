import React, { useEffect, useState } from 'react';
import { fetchOrderById, fetchApartmentById, fetchClientById } from '../api';

export default function OrderDetailsModal({ orderId, onClose, showClient }) {
  const [order, setOrder] = useState(null);
  const [apartment, setApartment] = useState(null);
  const [client, setClient] = useState(null);

  useEffect(() => {
    if (!orderId) return;

    fetchOrderById(orderId).then(o => {
      setOrder(o);
      if (o.apartmentId) fetchApartmentById(o.apartmentId).then(setApartment) 
        else setApartment(null);
      if (showClient) fetchClientById(o.clientId).then(setClient)
        else setClient(null);
    });
  }, [orderId, showClient]);

  if (!order) return <div className="modal">Загрузка...</div>;

  return (
    <div className="modal">
      <button onClick={onClose}>×</button>
      <h3>Ордер #{order.id} ({order.type})</h3>
      <p>Статус: {order.status}</p>
      <p>Диапазон цены: {order.priceMin} – {order.priceMax} ₽</p>

      {apartment && (
        <div className="apartment-info">
          <h4>Квартира</h4>
          <p>{apartment.address}</p>
          <p>{apartment.rooms} комн., {apartment.area} м², {apartment.floor} этаж</p>
          <p>Цена: {apartment.price} ₽</p>
        </div>
      )}

      {client && (
        <div className="client-info">
          <h4>Контрагент</h4>
          <p>{client.name}</p>
          <p>{client.phone}</p>
          <p>{client.email}</p>
        </div>
      )}
    </div>
  );
}
