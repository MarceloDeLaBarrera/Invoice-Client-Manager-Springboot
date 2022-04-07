package com.bolsadeideas.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

//Se le da este nombre porque es el nombre que retorna el metodo ver de factura. La idea es que en vez de retornar la pagina html, retorne el pdf.
@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//Trayendo el objeto factura con model.get de la vista "factura/ver".
		Factura factura= (Factura) model.get("factura");
		
		PdfPTable tabla= new PdfPTable(1); // 1 columna
		tabla.setSpacingAfter(20);
		
		PdfPCell cell = new PdfPCell(new Phrase("Datos del cliente"));
		cell.setBackgroundColor(new Color(184, 218, 255));
		cell.setPadding(8f);
		
		tabla.addCell(cell);
		tabla.addCell("Datos del Cliente");
		tabla.addCell(factura.getCliente().getNombre().concat(" ").concat(factura.getCliente().getApellido()));
		tabla.addCell(factura.getCliente().getEmail());
		
		PdfPTable tabla2= new PdfPTable(1);
		tabla2.setSpacingAfter(20);
		
		cell = new PdfPCell(new Phrase("Datos de la Factura"));
		cell.setBackgroundColor (new Color(195, 230, 203));
		cell.setPadding(8f);
		
		tabla2.addCell(cell);
		tabla2.addCell("Datos de la factura");
		tabla2.addCell("Folio: " + factura.getId());
		tabla2.addCell("Descripcion: "+factura.getDescripcion());
		tabla2.addCell("Fecha: "+factura.getCreateAt());
		
		PdfPTable tabla3 = new PdfPTable(4);
		tabla3.setWidths(new float [] {3.5f, 1, 1, 1}); //Establecer ancho de columnas.

		tabla3.addCell("Producto");
		tabla3.addCell("Precio");
		tabla3.addCell("Cantidad");
		tabla3.addCell("Total");
		for (ItemFactura item: factura.getItems()) {
		    tabla3.addCell(item.getProducto().getNombre());
		    tabla3.addCell(item.getProducto().getPrecio(). toString());
		    
		    cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			tabla3.addCell(cell);
			
		    tabla3.addCell(item.calcularImporte().toString());
			
		    
		}
		
		
		cell = new PdfPCell(new Phrase("Total: "));
		cell.setColspan (3); //Ancho de 3 columnas
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabla3.addCell(cell);
		tabla3.addCell(factura.getTotal().toString());
		
		document.add(tabla);
		document.add(tabla2);
		document.add(tabla3);
		
	}
	
}
