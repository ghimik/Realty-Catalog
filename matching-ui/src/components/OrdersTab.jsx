import React, { useState, useEffect } from 'react';
import { fetchClientOrders, createOrder, fetchClientApartments } from '../api';
import OrderForm from './OrderForm';
import MatchingOrders from './MatchingOrders';
import OrderDetailsModal from './OrderDetailsModal';
import '../styles/OrdersTab.css';

export default function OrdersTab({ client }) {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [editingOrder, setEditingOrder] = useState(null);
  const [matchingOrderId, setMatchingOrderId] = useState(null);
  const [apartments, setApartments] = useState([]);
  const [selectedOrderId, setSelectedOrderId] = useState(null);

  const loadApartments = async () => {
    setLoading(true);
    try {
      const data = await fetchClientApartments(client.id);
      setApartments(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const loadOrders = async () => {
    setLoading(true);
    try {
      const data = await fetchClientOrders(client.id);
      setOrders(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!client) return;
    loadOrders();
    loadApartments();
  }, [client]);

  const handleCreate = () => { setEditingOrder(null); setShowForm(true); };
  const handleEdit = (order) => { setEditingOrder(order); setShowForm(true); };
  const handleFormSubmit = async (dto) => { await createOrder({ ...dto, clientId: client.id }); setShowForm(false); loadOrders(); };
  const handleMatch = (orderId) => { setMatchingOrderId(orderId); };

  if (loading) return <p>Загрузка ордеров...</p>;
  if (!orders.length) return <p>Ордеров пока нет. <button onClick={handleCreate}>Создать</button></p>;

  return (
    <div className="orders-tab">
      <button onClick={handleCreate}>Создать ордер</button>
      <ul>
        {orders.map(o => (
          <li key={o.id} className="order-item">
            <span onClick={() => setSelectedOrderId(o.id)} className="clickable">
              {o.type} — {o.status} — {o.priceMin?.toLocaleString() || '—'} ₽ / {o.priceMax?.toLocaleString() || '—'} ₽
            </span>
            <div className="actions">
              <button onClick={() => handleEdit(o)}>Редактировать</button>
              <button onClick={() => handleMatch(o.id)}>Мэтчинг</button>
            </div>
          </li>
        ))}
      </ul>

      {showForm && <OrderForm apartments={apartments} order={editingOrder} onSubmit={handleFormSubmit} onClose={() => setShowForm(false)} />}
      {matchingOrderId && <MatchingOrders orderId={matchingOrderId} onClose={() => setMatchingOrderId(null)} />}
      {selectedOrderId && <OrderDetailsModal orderId={selectedOrderId} showClient={false} onClose={() => setSelectedOrderId(null)} />}
    </div>
  );
}
