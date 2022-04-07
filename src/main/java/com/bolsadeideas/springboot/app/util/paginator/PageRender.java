package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	
	//En esta clase se rendera la cantidad de paginas que se mostrarán por pagina, para ir cambiando de pagina.
	
	private String url;
	private Page<T> page;
	
	private int totalPaginas;
	private int numPaginasEnPagina;
	private int paginaActual;
	private List<PageItem> paginas;
	
	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas= new ArrayList<PageItem>();
		
		this.numPaginasEnPagina = page.getSize();
		this.totalPaginas = page.getTotalPages();
		this.paginaActual= page.getNumber()+1;
		
		int desde, hasta;
		if(totalPaginas <= numPaginasEnPagina) {
			//Si total de paginas es menor igual al numero de paginas visibles en pagina.
			desde=1;
			hasta= totalPaginas;
		} else {
			if(paginaActual<= numPaginasEnPagina/2) {
			//Si pagina actual es menor igual a la mitad de la cantidad de paginas a mostrar en pagina actual.
				//Ejemplo: Pagina actual 4, cantidad de paginas a mostrar 10. Significa que la cantidad total de paginas es menor que la
				desde=1;
				hasta= numPaginasEnPagina;
			} else if(paginaActual >= totalPaginas - numPaginasEnPagina/2 ) {
				desde= totalPaginas-numPaginasEnPagina+1;
				hasta= numPaginasEnPagina;
			} else {
				desde= paginaActual - numPaginasEnPagina/2;
				hasta= numPaginasEnPagina;
			}
		}
		
		//Con este form se agregan al array las paginas que se mostraran en la vista.
		for (int i =0; i<hasta; i++ ) {
			paginas.add(new PageItem(desde +i, paginaActual == desde+i));
		}
		
		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public void setPaginaActual(int paginaActual) {
		this.paginaActual = paginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}

	public void setPaginas(List<PageItem> paginas) {
		this.paginas = paginas;
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
}
