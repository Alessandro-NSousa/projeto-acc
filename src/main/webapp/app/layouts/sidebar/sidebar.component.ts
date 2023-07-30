// sidebar.component.ts
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
  ngOnInit(): void {
    // Nenhuma implementação JavaScript adicional é necessária.
    // O Bootstrap e o Angular cuidarão da lógica do offcanvas.
  }
}
