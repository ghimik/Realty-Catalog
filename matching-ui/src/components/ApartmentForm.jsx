import React, { useState, useEffect } from 'react';

export default function ApartmentForm({ apartment, onSubmit, onClose }) {
  const [form, setForm] = useState({
    address: '',
    rooms: 1,
    area: 0,
    floor: 1,
    price: 0
  });

  useEffect(() => {
    if (apartment) setForm(apartment);
  }, [apartment]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: name === 'rooms' || name === 'floor' ? parseInt(value) : name === 'area' || name === 'price' ? parseFloat(value) : value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(form);
  };

  return (
    <div className="apartment-form-backdrop">
      <form className="apartment-form" onSubmit={handleSubmit}>
        <h2>{apartment ? 'Редактировать' : 'Добавить'} квартиру</h2>
        <label>Адрес<input name="address" value={form.address} onChange={handleChange} required /></label>
        <label>Комнаты<input type="number" name="rooms" value={form.rooms} onChange={handleChange} min={1} required /></label>
        <label>Площадь (м²)<input type="number" name="area" value={form.area} onChange={handleChange} min={0} step={0.1} required /></label>
        <label>Этаж<input type="number" name="floor" value={form.floor} onChange={handleChange} min={0} required /></label>
        <label>Цена (₽)<input type="number" name="price" value={form.price} onChange={handleChange} min={0} required /></label>
        <div className="form-actions">
          <button type="submit">Сохранить</button>
          <button type="button" onClick={onClose}>Отмена</button>
        </div>
      </form>
    </div>
  );
}
