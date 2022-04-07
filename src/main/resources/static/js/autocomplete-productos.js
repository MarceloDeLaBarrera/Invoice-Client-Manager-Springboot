    $(document).ready(function(){
	$("#buscar_producto").autocomplete({
		
		source: function(request, response){
			$.ajax({
				url: "/factura/cargar-productos/" + request.term,
				dataType: "json",
				data: {
					term: request.term
				},
				success: function(data){
					response($.map(data, function(item){
						return {
							value: item.id,
							label: item.nombre,
							precio: item.precio,
						};
					}));
				},
			});
		},
		select: function(event, ui){
			$("#buscar_producto").val(ui.item.label);
			
			return false;
		}
	});
}
);
    
    