import React, { useState } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import ClientSelectPage from './pages/ClientSelectPage';
import ClientPage from './pages/ClientPage';
import './styles/App.css';

function App() {
  const [selectedClient, setSelectedClient] = useState(null);

  return (
    <div className="app">
      <Routes>
        <Route 
          path="/" 
          element={<ClientSelectPage setSelectedClient={setSelectedClient} />} 
        />
        <Route 
          path="/client" 
          element={
            selectedClient ? <ClientPage client={selectedClient} /> : <Navigate to="/" />
          } 
        />
      </Routes>
    </div>
  );
}

export default App;
