import React, { useState, useEffect } from 'react';
import { fetchMatchingOrders } from '../api';
import '../styles/MatchingOrders.css';

export default function MatchingOrders({ orderId, onCreateTrade, onClose }) {
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadMatches = async () => {
      setLoading(true);
      try {
        const data = await fetchMatchingOrders(orderId);
        setMatches(data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    loadMatches();
  }, [orderId]);

  if (loading) return <p>Загрузка предложений...</p>;
  if (!matches.length) return <p>Подходящих ордеров не найдено.</p>;

  return (
    <div className="matching-orders-backdrop">
      <div className="matching-orders">
        <h3>Подходящие ордера</h3>
        <ul>
          {matches.map(m => (
            <li key={m.id}>
              {m.type} — {m.priceMin?.toLocaleString() || '—'} ₽ / {m.priceMax?.toLocaleString() || '—'} ₽
              <button onClick={() => onCreateTrade(m.id)}>Предложить трейд</button>
            </li>
          ))}
        </ul>
        <button onClick={onClose}>Закрыть</button>
      </div>
    </div>
  );
}
