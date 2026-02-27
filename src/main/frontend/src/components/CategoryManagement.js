import React, { useState, useEffect } from 'react';
import ApiClient from '../api/ApiClient';
import './CategoryManagement.css';

const CategoryManagement = () => {
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [currentCategory, setCurrentCategory] = useState({
        name: '',
        type: 'MANDATORY',
        color: '#000000',
        active: true
    });
    const [isEditing, setIsEditing] = useState(false);

    const fetchCategories = async () => {
        try {
            setLoading(true);
            const data = await ApiClient.get('/categories');
            setCategories(data);
            setError(null);
        } catch (err) {
            setError('Failed to load categories');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    const handleOpenCreate = () => {
        setCurrentCategory({
            name: '',
            type: 'MANDATORY',
            color: '#000000',
            active: true
        });
        setIsEditing(false);
        setShowModal(true);
    };

    const handleOpenEdit = (category) => {
        setCurrentCategory({ ...category });
        setIsEditing(true);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setError(null);
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setCurrentCategory(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (isEditing) {
                await ApiClient.put('/category', currentCategory);
            } else {
                await ApiClient.post('/categories', currentCategory);
            }
            fetchCategories();
            handleCloseModal();
        } catch (err) {
            setError('Failed to save category');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this category?')) {
            try {
                const result = await ApiClient.delete(`/category?id=${id}`);
                if (result.error) {
                    setError(result.error);
                } else {
                    fetchCategories();
                }
            } catch (err) {
                setError('Failed to delete category');
            }
        }
    };

    return (
        <div className="category-management-container">
            <div className="card">
                <div className="card-header d-flex justify-content-between align-items-center">
                    <h5>Category Management</h5>
                    <button className="btn btn-sm btn-success" onClick={handleOpenCreate}>
                        + Add Category
                    </button>
                </div>
                <div className="card-body">
                    {error && <div className="alert alert-danger">{error}</div>}
                    {loading ? (
                        <p>Loading categories...</p>
                    ) : (
                        <div className="table-responsive">
                            <table className="table table-hover">
                                <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th>Type</th>
                                        <th>Color</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {categories.map(category => (
                                        <tr key={category.id}>
                                            <td>
                                                <span 
                                                    className="category-color-dot" 
                                                    style={{ backgroundColor: category.color }}
                                                ></span>
                                                {category.name}
                                            </td>
                                            <td>
                                                <span className={`badge category-type-badge ${
                                                    category.type === 'MANDATORY' ? 'bg-danger' : 
                                                    category.type === 'LEISURE' ? 'bg-info' : 'bg-primary'
                                                }`}>
                                                    {category.type}
                                                </span>
                                            </td>
                                            <td><code>{category.color}</code></td>
                                            <td>
                                                <button 
                                                    className="btn btn-sm btn-outline-primary me-2"
                                                    onClick={() => handleOpenEdit(category)}
                                                >
                                                    Edit
                                                </button>
                                                <button 
                                                    className="btn btn-sm btn-outline-danger"
                                                    onClick={() => handleDelete(category.id)}
                                                >
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            </div>

            {showModal && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h3>{isEditing ? 'Edit Category' : 'Create Category'}</h3>
                        <form onSubmit={handleSubmit}>
                            <div className="mb-3">
                                <label className="form-label">Name</label>
                                <input 
                                    type="text" 
                                    className="form-control" 
                                    name="name"
                                    value={currentCategory.name}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Type</label>
                                <select 
                                    className="form-select" 
                                    name="type"
                                    value={currentCategory.type}
                                    onChange={handleInputChange}
                                >
                                    <option value="MANDATORY">MANDATORY</option>
                                    <option value="LEISURE">LEISURE</option>
                                    <option value="INVESTMENTS">INVESTMENTS</option>
                                </select>
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Color</label>
                                <div className="d-flex align-items-center">
                                    <input 
                                        type="color" 
                                        className="form-control form-control-color me-2" 
                                        name="color"
                                        value={currentCategory.color}
                                        onChange={handleInputChange}
                                    />
                                    <span>{currentCategory.color}</span>
                                </div>
                            </div>
                            <div className="d-flex justify-content-end">
                                <button type="button" className="btn btn-secondary me-2" onClick={handleCloseModal}>
                                    Cancel
                                </button>
                                <button type="submit" className="btn btn-primary">
                                    {isEditing ? 'Update' : 'Create'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CategoryManagement;
