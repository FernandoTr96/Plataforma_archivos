<div class="filtros-container">
<form action="" method="POST">
    <label for="palabra">Buscador</label>
    <input type="search" name="palabra" id="palabra" placeholder="...">
    <button type="submit">
        <i class="fas fa-search"></i> Buscar 
    </button>
</form>
<div>
    <label for="prioridad">Buscar por prioridad</label>
    <select name="prioridad" id="prioridad"> 
        <option value="normal">Normal</option>
        <option value="urgente">urgente</option>
    </select>
</div>
<div>
    <label for="estado">Buscar por estado</label>
    <select name="estado" id="estado" multiple=""> 
        <option value="abierta">Publicacion abierta</option>
        <option value="cerrada">Publicacion cerrada</option>
        <option value="cerrada">Programadas</option>
    </select>
</div>
<div>
    <label for="fecha_fin">Buscar por fecha</label>
    <input type="date" name="fecha" id="fecha">
</div>
<div>
    <label>Publicaciones</label>
    <span>
        <input type="radio" name="orden" value="ASC"> Viejas
        <input type="radio" name="orden" value="DESC"> Nuevas
    </span>
</div>
</div>
