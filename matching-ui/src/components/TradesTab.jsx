import React, { useState, useEffect } from 'react';
import { fetchClientTrades, acceptTrade, rejectTrade } from '../api';
import OrderDetailsModal from './OrderDetailsModal';
import '../styles/TradesTab.css';

export default function TradesTab({ client }) {
  const [initiatedTrades, setInitiatedTrades] = useState([]);
  const [counterTrades, setCounterTrades] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedOrderId, setSelectedOrderId] = useState(null);
  const [showClient, setShowClient] = useState(false);

  useEffect(() => {
    if (!client) return;
    loadTrades();
  }, [client]);

  const loadTrades = async () => {
    setLoading(true);
    try {
      const [my, counter] = await Promise.all([
        fetchClientTrades(client.id, "clientId"),
        fetchClientTrades(client.id, "counterClientId")
      ]);
      setInitiatedTrades(my);
      setCounterTrades(counter);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleAccept = async (id) => {
    await acceptTrade(id);
    loadTrades();
  };

  const handleReject = async (id) => {
    await rejectTrade(id);
    loadTrades();
  };

  if (loading) return <p>Загрузка трейдов...</p>;
  if (!initiatedTrades.length && !counterTrades.length) return <p>Трейдов пока нет.</p>;

  return (
    <div className="trades-tab">
      <h3>Трейды, которые я выдвинул</h3>
      {initiatedTrades.length ? (
        <table className="trades-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Order</th>
              <th>Counter Order</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {initiatedTrades.map(t => (
              <tr key={t.id}>
                <td>{t.id}</td>
                <td onClick={() => { setSelectedOrderId(t.orderId); setShowClient(false); }} className="clickable">
                  {t.orderId}
                </td>
                <td onClick={() => { setSelectedOrderId(t.counterOrderId); setShowClient(true); }} className="clickable">
                  {t.counterOrderId}
                </td>
                <td>{t.status}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Нет инициированных трейдов.</p>
      )}

      <h3>Трейды, где я контрагент</h3>
      {counterTrades.length ? (
        <table className="trades-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Order</th>
              <th>Counter Order</th>
              <th>Status</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {counterTrades.map(t => (
              <tr key={t.id}>
                <td>{t.id}</td>
                <td onClick={() => { setSelectedOrderId(t.orderId); setShowClient(false); }} className="clickable">
                  {t.orderId}
                </td>
                <td onClick={() => { setSelectedOrderId(t.counterOrderId); setShowClient(true); }} className="clickable">
                  {t.counterOrderId}
                </td>
                <td>{t.status}</td>
                <td>
                  {t.status === 'PENDING' ? (
                    <>
                      <button onClick={() => handleAccept(t.id)}>Принять</button>
                      <button onClick={() => handleReject(t.id)}>Отклонить</button>
                    </>
                  ) : (
                    <span>—</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Нет трейдов, где вы контрагент.</p>
      )}

      {selectedOrderId && (
        <OrderDetailsModal
          orderId={selectedOrderId}
          showClient={showClient}
          onClose={() => setSelectedOrderId(null)}
        />
      )}
    </div>
  );
}
