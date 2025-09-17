import React, { useState, useEffect } from 'react';
import {
  fetchClientApartments,
  createApartment,
  updateApartment,
  deleteApartment
} from '../api';
import ApartmentForm from './ApartmentForm';
import '../styles/ApartmentsTab.css';

export default function ApartmentsTab({ client }) {
  const [apartments, setApartments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [editingApartment, setEditingApartment] = useState(null);
  const [showForm, setShowForm] = useState(false);

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

  useEffect(() => {
    if (!client) return;
    loadApartments();
  }, [client]);

  const handleCreate = () => {
    setEditingApartment(null);
    setShowForm(true);
  };

  const handleEdit = (apartment) => {
    setEditingApartment(apartment);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Вы уверены, что хотите удалить эту квартиру?')) {
      await deleteApartment(id);
      loadApartments();
    }
  };

  const handleFormSubmit = async (dto) => {
    if (editingApartment) {
      await updateApartment(editingApartment.id, dto);
    } else {
      await createApartment({ ...dto, ownerId: client.id });
    }
    setShowForm(false);
    loadApartments();
  };

  if (loading) return <p>Загрузка квартир...</p>;
  if (!apartments.length) return <p>Квартир пока нет. <button onClick={handleCreate}>Добавить</button></p>;

  return (
    <div className="apartments-tab">
      <button onClick={handleCreate}>Добавить квартиру</button>
      <ul>
        {apartments.map(a => (
          <li key={a.id} className="apartment-item">
            {a.address} — {a.rooms} комн., {a.area} м² — {a.price.toLocaleString()} ₽
            <div className="actions">
              <button onClick={() => handleEdit(a)}>Редактировать</button>
              <button onClick={() => handleDelete(a.id)}>Удалить</button>
            </div>
          </li>
        ))}
      </ul>
      {showForm && <ApartmentForm apartment={editingApartment} onSubmit={handleFormSubmit} onClose={() => setShowForm(false)} />}
    </div>
  );
}
