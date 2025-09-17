import React, { useState, useEffect } from 'react';
import '../styles/OrderForm.css';

export default function OrderForm({ apartments, order, onSubmit, onClose }) {
  const [form, setForm] = useState({
    type: 'BUY',
    apartmentId: apartments?.[0]?.id || null,
    priceMin: 0,
    priceMax: 0,
  });

  useEffect(() => {
    if (order) setForm(order);
  }, [order]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: name.includes('price') ? parseFloat(value) : value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(form);
  };

  return (
    <div className="order-form-backdrop">
      <form className="order-form" onSubmit={handleSubmit}>
        <h2>{order ? 'Редактировать' : 'Создать'} ордер</h2>

        <label>Тип
          <select name="type" value={form.type} onChange={handleChange}>
            <option value="BUY">Покупка</option>
            <option value="SELL">Продажа</option>
            <option value="EXCHANGE">Обмен</option>
          </select>
        </label>

        {(form.type === 'SELL' || form.type === 'EXCHANGE') && (
          <label>Квартира
            <select name="apartmentId" value={form.apartmentId} onChange={handleChange}>
              {apartments.map(a => (
                <option key={a.id} value={a.id}>{a.address}</option>
              ))}
            </select>
          </label>
        )}

        {(form.type !== 'EXCHANGE') && (<>
            <label>Цена мин
                <input type="number" name="priceMin" value={form.priceMin} onChange={handleChange} />
            </label>
            <label>Цена макс
                <input type="number" name="priceMax" value={form.priceMax} onChange={handleChange} />
            </label></>
        )}

        <div className="form-actions">
          <button type="submit">Сохранить</button>
          <button type="button" onClick={onClose}>Отмена</button>
        </div>
      </form>
    </div>
  );
}
