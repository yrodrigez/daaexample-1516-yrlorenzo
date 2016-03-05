var peopleFormId = 'people-form';
var peopleListId = 'people-list';
var petsTable = 'pets-table';
var petsFormId= 'pets-form';
var petsFormQuery= '#'+petsFormId;
var petsTableQuery = '#' + petsTable;
var peopleFormQuery = '#' + peopleFormId;
var peopleListQuery = '#' + peopleListId;

function insertPeopleList(parent) {
	parent.append(
		'<table class="rwd-table" id="' + peopleListId + '">\
			<tr>\
				<th>Nombre</th>\
				<th>Apellido</th>\
				<th></th>\
				<th></th>\
			</tr>\
		</table>'
	);
}

function insertPeopleForm(parent) {
	parent.append(
		'<form id="' + peopleFormId + '">\
			<input name="id" type="hidden" value=""/>\
			<input name="name" type="text" value="" />\
			<input name="surname" type="text" value=""/>\
			<input id="btnSubmit" type="submit" value="Create"/>\
			<input id="btnClear" type="reset" value="Limpiar"/>\
		</form>'
	);
}

function deletePetsForm(){
	$("form#" + petsFormId).remove();
}



function createPeopleOptions(){
	listPeople(function (people) {
		$.each(people, function (key, person) {
			$("select#owners").append(
				'<option value="'+person.id+'">'+person.name+'</option>'
			);
		});
	});
}


function createPersonRow(person) {
	return '<tr id="person-'+ person.id +'">\
		<td class="name">' + person.name + '</td>\
		<td class="surname">' + person.surname + '</td>\
		<td>\
			<a class="edit" href="#">Edit</a>\
		</td>\
		<td>\
			<a class="delete" href="#">Delete</a>\
		</td>\
		<td>\
			<a class="pets" href="#">Pets</a>\
		</td>\
	</tr>';
}

function formToPerson() {
	var form = $(peopleFormQuery);
	return {
		'id': form.find('input[name="id"]').val(),
		'name': form.find('input[name="name"]').val(),
		'surname': form.find('input[name="surname"]').val()
	};
}

function personToForm(person) {
	var form = $(peopleFormQuery);
	form.find('input[name="id"]').val(person.id);
	form.find('input[name="name"]').val(person.name);
	form.find('input[name="surname"]').val(person.surname);
}

function rowToPerson(id) {
	var row = $('#person-' + id);

	return {
		'id': id,
		'name': row.find('td.name').text(),
		'surname': row.find('td.surname').text()
	};
}

function isEditing() {
	return $(peopleFormQuery + ' input[name="id"]').val() != "";
}
function isEditingPet(){
	return $(petsFormQuery + ' input[name="petId"]').val() != "";
}

function disableForm() {
	$(peopleFormQuery + ' input').prop('disabled', true);
}

function enableForm() {
	$(peopleFormQuery + ' input').prop('disabled', false);
}

function resetForm() {
	$(peopleFormQuery)[0].reset();
	$(peopleFormQuery + ' input[name="id"]').val('');
	$('#btnSubmit').val('Crear');
}
function resetPetForm() {
	$(petsFormQuery)[0].reset();
	$(petsFormQuery + ' input[name="petId"]').val('');
	$('#petBtnSubmit').val('Crear');
}

function showErrorMessage(jqxhr, textStatus, error) {
	alert(textStatus + ": " + error);
}

function addRowListeners(person) {
	$('#person-' + person.id + ' a.edit').click(function() {
		personToForm(rowToPerson(person.id));
		$('input#btnSubmit').val('Modificar');
	});

	$('#person-' + person.id + ' a.delete').click(function() {
		if (confirm('Está a punto de eliminar a una persona. ¿Está seguro de que desea continuar?')) {
			deletePerson(person.id,
				function() {
					$('tr#person-' + person.id).remove();
				},
				showErrorMessage
			);
		}
	});

	$('#person-' + person.id + ' a.pets').click(function() {
		showPersonsPets(rowToPerson(person.id))
	});
}

function addPetRowListener (pet){
	$('#pet-' + pet.id + ' a.deletePet').click(function() {
		if(confirm('Estás eliminando a: '+pet.name+". ¿Estás seguro?")) {
			deletePet(pet.id,
				function () {
					$('tr#pet-' + pet.id).remove();
				},
				showErrorMessage
			);
		}
	});

	$('#pet-' + pet.id + ' a.editPet').click(function() {
		petToForm(pet);
		$('input#petBtnSubmit').val('Modificar');
	});
}

function insertPetsForm() {
	$("body").append(
		'<form id="' + petsFormId + '">\
			<input name="petId" type="hidden" value=""/>\
			Nombre: <input name="petName" type="text" value="" />\
			Raza: <input name="breed" type="text" value="" />\
			Animal: <input name="animal" type="text" value="" />\
			Dueño: <select name="ownerId" id="owners"></select>\
			<input id="petBtnSubmit" type="submit" value="Crear"/>\
			<input id="btnClear" type="reset" value="Limpiar"/>\
		</form>'
	);
	createPeopleOptions();
}
function petToForm(pet){
	var form = $(petsFormQuery);
	form.find('input[name="petId"]').val(pet.id);
	form.find('input[name="petName"]').val(pet.name);
	form.find('input[name="breed"]').val(pet.breed);
	form.find('input[name="animal"]').val(pet.animal);
	form.find('select[name="ownerId"]').val(pet.ownerId);
}

function formToPet(){
	var form = $(petsFormQuery);
	/*alert(form.find('input[name=petId]').val());
	alert(form.find('input[name=petName]').val());
	alert(form.find('input[name=breed]').val());
	alert(form.find('input[name=animal]').val());
	alert(form.find('select[name="ownerId"]').val());*/
	return {
		'id': form.find('input[name=petId]').val(),
		'name': form.find('input[name=petName]').val(),
		'breed': form.find('input[name=breed]').val(),
		'animal': form.find('input[name=animal]').val(),
		'ownerId': form.find('select[name="ownerId"]').val()
	};
}


function showPersonsPets(person){
	$.getScript('js/dao/pets.js',function() {
		listPersonsPets(person.id, function (pets) {
			deletePetsTable();
			createPetsTable(person.name);
			$.each(pets, function (key, pet) {
				addPetToTable(pet);
			});
		});

		$(petsFormQuery).submit(function(event) {
			var pet = formToPet();
			if (isEditingPet()) {
				modifyPet(pet,
					function(pet) {
						$('#pet-' + pet.id + ' td.name').text(pet.name);
						$('#pet-' + pet.id + ' td.breed').text(pet.breed);
						$('#pet-' + pet.id + ' td.animal').text(pet.animal);
						resetPetForm();
					},
					showErrorMessage,
					enableForm
				);
			} else {
				addPet(pet,
					function(pet){
						alert("adding pet!");
						$('#pet-' + pet.id + ' td.name').text(pet.name);
						$('#pet-' + pet.id + ' td.breed').text(pet.breed);
						$('#pet-' + pet.id + ' td.animal').text(pet.animal);
						resetPetForm();
					},
					showErrorMessage,
					enableForm
				);
			}
			return false;
		});
	});
}
function addPetToTable(pet){
	$(petsTableQuery  + ' > tbody:last').append(petToRow(pet));
	addPetRowListener(pet);
}

function petToRow(pet){
	return '<tr id="pet-'+ pet.id +'">\
		<td class="name">' + pet.name + '</td>\
		<td class="breed">' + pet.breed + '</td>\
		<td class="animal">' + pet.animal + '</td>\
		<input id="ownerId" type="hidden" value="'+pet.ownerId+'">\
		<td>\
			<a class="editPet" href="#">Edit</a>\
		</td>\
		<td>\
			<a class="deletePet" href="#">Delete</a>\
		</td>\
	</tr>';
}

function deletePetsTable(){
	$(petsTableQuery).remove();
}

function createPetsTable(owner){
	$('div').append(
		'<table class="rwd-table" id="' + petsTable + '">\
		<tr class="owner">\
			<th>Mascotas de: '+owner+'</th>\
			<th></th>\
			<th></th>\
			<th></th>\
			<th></th>\
		</tr>\
		<tr>\
			<th>Nombre</th>\
			<th>Raza</th>\
			<th>animal</th>\
			<th>Opciones</th>\
			<th></th>\
		</tr>\
		</table>'
	);
}

function appendToTable(person) {
	$(peopleListQuery + ' > tbody:last')
		.append(createPersonRow(person));
	addRowListeners(person);
}



function initPeople() {
	// getScript permite importar otro script. En este caso, se importan las
	// funciones de acceso a datos.
	$.getScript('js/dao/people.js', function() {
		listPeople(function(people) {
			$.each(people, function(key, person) {
				appendToTable(person);
			});
		});

		// La acción por defecto de enviar formulario (submit) se sobreescribe
		// para que el envío sea a través de AJAX
		$(peopleFormQuery).submit(function(event) {
			var person = formToPerson();

			if (isEditing()) {
				modifyPerson(person,
					function(person) {
						$('#person-' + person.id + ' td.name').text(person.name);
						$('#person-' + person.id + ' td.surname').text(person.surname);
						resetForm();
					},
					showErrorMessage,
					enableForm
				);
			} else {
				addPerson(person,
					function(person) {
						appendToTable(person);
						resetForm();
					},
					showErrorMessage,
					enableForm
				);
			}

			return false;
		});

		insertPetsForm();
		$('#btnClear').click(resetForm);
	});
}
