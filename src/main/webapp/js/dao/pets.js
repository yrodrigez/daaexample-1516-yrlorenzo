/**
 * Created by yago on 25/02/16.
 */
function listPersonsPets(personId, done, fail, always) {
    done = typeof done !== 'undefined' ? done : function() {};
    fail = typeof fail !== 'undefined' ? fail : function() {};
    always = typeof always !== 'undefined' ? always : function() {};

    $.ajax({
            url: 'rest/pets/'+personId,
            type: 'GET'
        })
        .done(done)
        .fail(fail)
        .always(always);
}

function deletePet(petId, done, fail, always){
    done = typeof done !== 'undefined' ? done : function() {};
    fail = typeof fail !== 'undefined' ? fail : function() {};
    always = typeof always !== 'undefined' ? always : function() {};

    $.ajax({
            url: 'rest/pets/' + petId,
            type: 'DELETE'
        })
        .done(done)
        .fail(fail)
        .always(always);
}

function modifyPet(pet, done, fail, always) {
    done = typeof done !== 'undefined' ? done : function() {};
    fail = typeof fail !== 'undefined' ? fail : function() {};
    always = typeof always !== 'undefined' ? always : function() {};

    $.ajax({
            url: 'rest/pet/' + pet.id,
            type: 'PUT',
            data: pet
        })
        .done(done)
        .fail(fail)
        .always(always);
}

function addPet(pet, done, fail, always) {
    done = typeof done !== 'undefined' ? done : function() {};
    fail = typeof fail !== 'undefined' ? fail : function() {};
    always = typeof always !== 'undefined' ? always : function() {};

    $.ajax({
            url: 'rest/pet/',
            type: 'POST',
            data: pet
        })
        .done(done)
        .fail(fail)
        .always(always);
}