const BASE_URL = "http://localhost:8080";

const REGISTRY_URL = BASE_URL + '/registry';
const MATCHING_URL = BASE_URL + '/matching'

export async function fetchClients() {
  try {
    const res = await fetch(`${REGISTRY_URL}/clients`);
    if (!res.ok) throw new Error("Ошибка при загрузке клиентов");
    return res.json();
  } catch (err) {
    console.error(err);
    return [];
  }
}


export async function fetchClientApartments(clientId) {
  const res = await fetch(`${REGISTRY_URL}/apartments?ownerId=${clientId}`);
  if (!res.ok) throw new Error('Ошибка при загрузке квартир');
  return res.json();
}

export async function fetchClientOrders(clientId) {
  const res = await fetch(`${MATCHING_URL}/api/orders/search?clientId=${clientId}`);
  if (!res.ok) throw new Error('Ошибка при загрузке ордеров');
  return res.json();
}

export async function fetchClientTrades(clientId, mode = "clientId") {
  const res = await fetch(`${MATCHING_URL}/api/trades/search?${mode}=${clientId}`);
  if (!res.ok) throw new Error('Failed to load trades');
  return res.json();
}


// APARTMENTS CRUD


export async function createApartment(dto) {
  const res = await fetch(`${REGISTRY_URL}/apartments`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dto),
  });
  if (!res.ok) throw new Error('Ошибка при создании квартиры');
  return res.json();
}

export async function updateApartment(id, dto) {
  const res = await fetch(`${REGISTRY_URL}/apartments/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dto),
  });
  if (!res.ok) throw new Error('Ошибка при обновлении квартиры');
  return res.json();
}

export async function deleteApartment(id) {
  const res = await fetch(`${REGISTRY_URL}/apartments/${id}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Ошибка при удалении квартиры');
}

// ORDERS TO TRADES MANAGMENT

export async function createOrder(dto) {
  const res = await fetch(`${MATCHING_URL}/api/orders`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dto),
  });
  if (!res.ok) throw new Error('Ошибка при создании ордера');
  return res.json();
}

export async function fetchMatchingOrders(orderId) {
  const res = await fetch(`${MATCHING_URL}/api/orders/${orderId}/match`);
  if (!res.ok) throw new Error('Ошибка при загрузке мэтчинга');
  return res.json();
}

export async function createTrade(orderId, counterOrderId) {
  const res = await fetch(`${MATCHING_URL}/api/trades?orderId=${orderId}&counterOrderId=${counterOrderId}`, {
    method: 'POST',
  });
  if (!res.ok) throw new Error('Ошибка при создании трейда');
  return res.json();
}

// TRADES


export async function acceptTrade(tradeId) {
  const res = await fetch(`${MATCHING_URL}/api/trades/${tradeId}/accept`, { method: "POST" });
  if (!res.ok) throw new Error("Ошибка при принятии трейда");
  return res.json();
}

export async function rejectTrade(tradeId) {
  const res = await fetch(`${MATCHING_URL}/api/trades/${tradeId}/reject`, { method: "POST" });
  if (!res.ok) throw new Error("Ошибка при отклонении трейда");
  return res.json();
}

// UTILS 


export async function deleteOrderById(id) {
  const res = await fetch(`${MATCHING_URL}/api/orders/${id}`, {method: "DELETE"});
  return;
}

export async function fetchOrderById(id) {
  const res = await fetch(`${MATCHING_URL}/api/orders/${id}`);
  return res.json();
}

export async function fetchApartmentById(id) {
  const res = await fetch(`${REGISTRY_URL}/apartments/${id}`);
  return res.json();
}

export async function fetchClientById(id) {
  const res = await fetch(`${REGISTRY_URL}/clients/${id}`);
  return res.json();
}
