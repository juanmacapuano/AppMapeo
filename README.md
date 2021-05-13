# AppMapeo

Mapeo App es una aplicación desarrollada íntegramente por el INCUAPA para el registro arqueológico y paleontológico en el campo en Kotlin. 
<p>La app permite agregar sitios Arqueológicos y dentro de cada uno crear registros de mapeos. 
Cada mapeo contiene datos de extracciones y hallazgos arqueológicos y paleontológicos y permite tomar una fotografía del hallazgo y poder editarla (dibujar, borrar, agregar texto).
La app permite la exportación de los datos en formato .csv </p>
Para el diseño de la app se siguió el principio de MVVM con DataBinding y los datos se persisten con Room Database. Se utilizaron componentes Jeckpack (Navigation, Room, Material Design, Fragment, CardView)

<p>Desarrollo:</p>
<p>La app comienza con el listado de sitios arqueológicos creados. Pudiendo agregar nuevos o ingresar a alguno determinado.
Ingresando a un sitio en particular, aparece el listado de todos los mapeos creados para ese sitio. Pudiendo agregar uno nuevo o modificar alguno existente. 
Al cada mapeo se puede tomar una o varias fotos y editarlas agregando información como texto o dibujos.</p>

**ProjectsListActivity.kt**
<p>Contiene el fragment_container el cual aloja el componente Navigation.</p>

**ProjectListFragment.kt**

<p>Contiene el ProjectListViewModel que es el viewModel compartido para toda app (habría que separar el viewModel, uno para Proyectos y otro para Mapeos)
Este fragment observa la lista de todos los proyectos para poder mostrarla en un RecyclerView.
El componente Navigation maneja el cambio de fragment cuando se presiona un ítem del RecyclerView.</p>

**ProjectItemFragment.kt**
<p>Contiene el ProjectListViewModel que es el viewModel compartido para toda app.
Este fragment determina si se está creando un nuevo proyecto o se quiere editar uno en particular. Esta consulta la maneja el ViewModel.</p>

**ProjectListViewModel.kt**
<p>Contiene los campos del Proyecto y del Mapeo como LiveData.
Se comunica con el repositorio para hacer las consultas de los RecyclerView y para hacer el ABM de la BD.
Claramente debería existir un viewModel para los Proyectos y otro para los Mapeos</p>


Creación de Sitios:

![img](https://i.imgur.com/wpABhGk.png) ![img](https://i.imgur.com/NkAjW4V.png)

Creación de Mapeos:

![img](https://i.imgur.com/PnWoHz3.png) ![img](https://i.imgur.com/erfpsYn.png)

Edición de fotos:

![img](https://i.imgur.com/ahLAT37.png)

