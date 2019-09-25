
$(document).ready(function() {
    var listaarchivos=new Array();
    var jsonarchivo={};
	$("#fileList").on("click",".btnEliminar",function(){
		console.log("Entrada");
		var id=$(this).attr("data-atr");
		console.log("id",id);
        listaarchivos.splice(id, 1);
        console.log("lista de archivos",listaarchivos);
		$("#row"+id).remove();
    });

	$("#btnAgregar").click(function(){
		var archivo={};
        var file=$("#customFile").get(0).files[0];
		archivo.descripcion=$("#nombreId").val();
        archivo.file=file;
        agregarLista(listaarchivos.length,archivo);
        listaarchivos.push(archivo);
        limpiarData();
        console.log("Archivos cargados",listaarchivos);
	});

	$("#btnUploadFile").click(function(){
		var url="http://localhost:8090/files/upload";
		console.log(listaarchivos);
        var file=$("#customFile").get(0).files[0];
        var name=$("#nombreId").val();
        var formData = new FormData();
        var arr= new Array();
        for(archivo of listaarchivos){
            var datos={};
            datos.descripcion=archivo.descripcion;
            arr.push(datos)
            formData.append("file",archivo.file);
		}
        var entityJsonStr = JSON.stringify(arr);
        formData.append('archivo',new Blob([entityJsonStr], {
            type: "application/json"
        }));

        console.log("form",formData);
        fetch(url,{
            method: "post",
            //enctype: 'multipart/form-data',
            body: formData
        }).then(function (data) {
            console.log("SUCCESS : ", data);
            $("#btnSubmit").prop("disabled", false);
            listUploadedFiles();
        });

	})

	$('#btnUploadFile').attr("disabled", true);
	$("#customFile").change(function() {
		var fileName = $(this).val().replace(/C:\\fakepath\\/i, '');

		var ext = fileName.split('.').pop().toLowerCase();
		console.log("ext",ext);
		if (ext == 'pdf') {
			$("#customFileHolder").val(fileName);
			$('#btnUploadFile').removeAttr("disabled");
		}
	});

	if ($('#divUploadSuccess').is(':visible')) {
		$('#divUploadSuccess').show(0).delay(10000).hide(0);
	}


    listUploadedFiles();
});

function limpiarData(){
    $("#nombreId").val(undefined);
    $("#customFileHolder").val("");
    $("#customFile").val("");
}

function listUploadedFiles() {
	$.ajax({
		url : $('#urlFileList').val(),
		error : function() {
			$('#fileList').html('<p>An error has occurred</p>');
		},
		success : function(data) {
			console.log(data);
			$('#fileList').html(data);
		},
		type : 'GET'
	});
}

function agregarLista(tamaño,archivo){
    var id=tamaño;
	$("#tblFileList tbody").append("<tr id='row"+id+"'><td>"+archivo.descripcion+"</td><td>"+archivo.file.name+"</td><td><a data-atr='"+id+"' class='btn btn-primary btnEliminar'><i class='far fa-trash-alt'></i></a></td></tr>");
}

