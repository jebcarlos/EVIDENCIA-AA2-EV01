// ========== Configuración ==========
const API_BASE_URL = '/api/usuarios';
let currentEditingId = null;

// ========== Inicialización ==========
document.addEventListener('DOMContentLoaded', function() {
    loadUsuarios();
    setupEventListeners();
});

function setupEventListeners() {
    // Botones principales
    document.getElementById('createBtn').addEventListener('click', openCreateModal);
    document.getElementById('searchBtn').addEventListener('click', searchByIdentificacion);
    document.getElementById('resetBtn').addEventListener('click', resetSearch);

    // Formulario
    document.getElementById('userForm').addEventListener('submit', handleFormSubmit);

    // Modal
    document.querySelectorAll('[data-close="true"]').forEach(btn => {
        btn.addEventListener('click', closeModal);
    });

    // Búsqueda con Enter
    document.getElementById('searchInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            searchByIdentificacion();
        }
    });
}

// ========== Carga de Datos ==========
function loadUsuarios() {
    showLoading(true);
    fetch(`${API_BASE_URL}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        showLoading(false);
        if (data.success) {
            renderTable(data.data);
        } else {
            showAlert(data.message || 'Error al cargar usuarios', 'error');
        }
    })
    .catch(error => {
        showLoading(false);
        showAlert(`Error de conexión: ${error.message}`, 'error');
    });
}

function renderTable(usuarios) {
    const tbody = document.getElementById('tableBody');
    
    if (!usuarios || usuarios.length === 0) {
        tbody.innerHTML = '<tr class="empty-row"><td colspan="6">No hay usuarios. Crea uno nuevo para comenzar.</td></tr>';
        return;
    }

    tbody.innerHTML = usuarios.map(usuario => `
        <tr>
            <td>${usuario.usuConsecutivo}</td>
            <td>${getDocumentType(usuario.tpd)}</td>
            <td>${usuario.identificacion}</td>
            <td>${usuario.primerNombre} ${usuario.segundoNombre ? usuario.segundoNombre + ' ' : ''}${usuario.primerApellido} ${usuario.segundoApellido || ''}</td>
            <td>-</td>
            <td>
                <div class="action-buttons">
                    <button class="btn btn-edit" onclick="editUsuario(${usuario.usuConsecutivo})">✏️ Editar</button>
                    <button class="btn btn-delete" onclick="openDeleteModal(${usuario.usuConsecutivo}, '${usuario.primerNombre} ${usuario.primerApellido}')">🗑️ Eliminar</button>
                </div>
            </td>
        </tr>
    `).join('');
}

function getDocumentType(tpd) {
    const types = {
        1: 'CC',
        2: 'CE',
        3: 'PAS',
        4: 'NIT'
    };
    return types[tpd] || `Tipo ${tpd}`;
}

// ========== Búsqueda ==========
function searchByIdentificacion() {
    const identificacion = document.getElementById('searchInput').value.trim();
    
    if (!identificacion) {
        showAlert('Ingresa una identificación para buscar', 'warning');
        return;
    }

    showLoading(true);
    fetch(`${API_BASE_URL}/buscar/identificacion/${encodeURIComponent(identificacion)}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        showLoading(false);
        if (data.success) {
            renderTable(data.data);
            showAlert(`Se encontraron ${data.data.length} usuario(s)`, 'info');
        } else {
            renderTable([]);
            showAlert(data.message || 'No se encontraron usuarios', 'warning');
        }
    })
    .catch(error => {
        showLoading(false);
        showAlert(`Error en la búsqueda: ${error.message}`, 'error');
    });
}

function resetSearch() {
    document.getElementById('searchInput').value = '';
    loadUsuarios();
}

// ========== Modal: Crear/Editar ==========
function openCreateModal() {
    currentEditingId = null;
    document.getElementById('modalTitle').textContent = 'Crear Nuevo Usuario';
    document.getElementById('userId').value = '';
    document.getElementById('userForm').reset();
    document.getElementById('userModal').classList.remove('hidden');
}

function editUsuario(id) {
    currentEditingId = id;
    showLoading(true);

    fetch(`${API_BASE_URL}/${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        showLoading(false);
        if (data.success) {
            const usuario = data.data;
            document.getElementById('modalTitle').textContent = 'Editar Usuario';
            document.getElementById('userId').value = usuario.usuConsecutivo;
            document.getElementById('tpd').value = usuario.tpd;
            document.getElementById('identificacion').value = usuario.identificacion;
            document.getElementById('dv').value = usuario.dv || '';
            document.getElementById('primerApellido').value = usuario.primerApellido;
            document.getElementById('segundoApellido').value = usuario.segundoApellido || '';
            document.getElementById('primerNombre').value = usuario.primerNombre;
            document.getElementById('segundoNombre').value = usuario.segundoNombre || '';
            document.getElementById('fechaNacimiento').value = usuario.fechaNacimiento || '';
            document.getElementById('sexo').value = usuario.sexo || '';
            document.getElementById('tipoSangre').value = usuario.tipoSangre || '';
            
            document.getElementById('userModal').classList.remove('hidden');
        } else {
            showAlert(data.message || 'Error al cargar el usuario', 'error');
        }
    })
    .catch(error => {
        showLoading(false);
        showAlert(`Error: ${error.message}`, 'error');
    });
}

function closeModal() {
    document.getElementById('userModal').classList.add('hidden');
    currentEditingId = null;
}

// ========== Formulario Envío ==========
function handleFormSubmit(e) {
    e.preventDefault();

    const formData = {
        tpd: parseInt(document.getElementById('tpd').value),
        identificacion: document.getElementById('identificacion').value.trim(),
        dv: document.getElementById('dv').value ? parseInt(document.getElementById('dv').value) : null,
        primerApellido: document.getElementById('primerApellido').value.trim(),
        segundoApellido: document.getElementById('segundoApellido').value.trim() || null,
        primerNombre: document.getElementById('primerNombre').value.trim(),
        segundoNombre: document.getElementById('segundoNombre').value.trim() || null,
        fechaNacimiento: document.getElementById('fechaNacimiento').value || null,
        sexo: document.getElementById('sexo').value || null,
        tipoSangre: document.getElementById('tipoSangre').value ? parseInt(document.getElementById('tipoSangre').value) : null
    };

    // Validaciones
    if (!formData.identificacion) {
        showAlert('La identificación es requerida', 'error');
        return;
    }
    if (!formData.primerApellido) {
        showAlert('El primer apellido es requerido', 'error');
        return;
    }
    if (!formData.primerNombre) {
        showAlert('El primer nombre es requerido', 'error');
        return;
    }

    if (currentEditingId) {
        updateUsuario(currentEditingId, formData);
    } else {
        createUsuario(formData);
    }
}

function createUsuario(formData) {
    showLoading(true);
    fetch(API_BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => {
        showLoading(false);
        if (data.success) {
            showAlert('✓ Usuario creado exitosamente', 'success');
            closeModal();
            loadUsuarios();
        } else {
            showAlert(data.message || 'Error al crear usuario', 'error');
        }
    })
    .catch(error => {
        showLoading(false);
        showAlert(`Error: ${error.message}`, 'error');
    });
}

function updateUsuario(id, formData) {
    showLoading(true);
    fetch(`${API_BASE_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => {
        showLoading(false);
        if (data.success) {
            showAlert('✓ Usuario actualizado exitosamente', 'success');
            closeModal();
            loadUsuarios();
        } else {
            showAlert(data.message || 'Error al actualizar usuario', 'error');
        }
    })
    .catch(error => {
        showLoading(false);
        showAlert(`Error: ${error.message}`, 'error');
    });
}

// ========== Modal: Confirmación Eliminación ==========
function openDeleteModal(id, nombre) {
    document.getElementById('deleteMessage').textContent = 
        `¿Estás seguro de que deseas eliminar a ${nombre}? Esta acción no se puede deshacer.`;
    
    document.getElementById('confirmDeleteBtn').onclick = function() {
        deleteUsuario(id);
    };
    
    document.getElementById('deleteConfirmModal').classList.remove('hidden');
}

function closeDeleteModal() {
    document.getElementById('deleteConfirmModal').classList.add('hidden');
}

function deleteUsuario(id) {
    showLoading(true);
    fetch(`${API_BASE_URL}/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        showLoading(false);
        if (data.success) {
            showAlert('✓ Usuario eliminado exitosamente', 'success');
            closeDeleteModal();
            loadUsuarios();
        } else {
            showAlert(data.message || 'Error al eliminar usuario', 'error');
        }
    })
    .catch(error => {
        showLoading(false);
        showAlert(`Error: ${error.message}`, 'error');
    });
}

// ========== UI Helpers ==========
function showLoading(show) {
    document.getElementById('loadingSpinner').classList.toggle('hidden', !show);
}

function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alertContainer');
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.textContent = message;

    alertContainer.appendChild(alert);

    // Auto remove after 5 seconds
    setTimeout(() => {
        alert.style.opacity = '0';
        alert.style.transition = 'opacity 0.3s ease';
        setTimeout(() => alert.remove(), 300);
    }, 5000);
}
