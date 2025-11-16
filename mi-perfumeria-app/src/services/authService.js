import axios from 'axios';

const API_URL = 'http://localhost:1010/api/perfumeria/usuario';

// Login
export const login = async (email, password) => {
    const response = await axios.post(`${API_URL}/login`, {
        email,
        password
    });
    return response.data;
};

export const register = async (nombre, email, numeroTelefono, password, role = 'USER') => {
    const response = await axios.post(`${API_URL}/save`, {
        nombre,
        email,
        numeroTelefono,
        password,
        role
    });
    return response.data;
};

// ADMIN: Listar usuarios
export const getAllUsers = async (page = 0, size = 10) => {
    const response = await axios.get(`${API_URL}/usuarios`, {
        params: { page, size }
    });
    return response.data;
};

// ADMIN: Obtener usuario por ID
export const getUserById = async (id) => {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
};

// ADMIN: Actualizar usuario
export const updateUser = async (userData) => {
    const response = await axios.put(`${API_URL}/update`, userData);
    return response.data;
};

// ADMIN: Eliminar usuario
export const deleteUser = async (id) => {
    const response = await axios.delete(`${API_URL}/delete/${id}`);
    return response.data;
};
