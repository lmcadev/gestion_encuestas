import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import Login from '../Login.jsx';

describe('Login page', () => {
  it('renders the login form and stores token on success', async () => {
    // Mock fetch
    global.fetch = jest.fn(() =>
      Promise.resolve({ ok: true, json: () => Promise.resolve({ token: 'abc' }) }),
    );
    const { getByLabelText, getByRole } = render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>,
    );
    fireEvent.change(getByLabelText(/usuario/i), { target: { value: 'admin' } });
    fireEvent.change(getByLabelText(/contraseña/i), { target: { value: 'secret' } });
    fireEvent.click(getByRole('button', { name: /entrar/i }));
    await waitFor(() => {
      expect(localStorage.getItem('token')).toBe('abc');
    });
    global.fetch.mockClear();
  });
});