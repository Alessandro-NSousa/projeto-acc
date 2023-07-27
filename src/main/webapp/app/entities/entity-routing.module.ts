import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'usuario',
        data: { pageTitle: 'controleDeAccApp.usuario.home.title' },
        loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'certificado',
        data: { pageTitle: 'controleDeAccApp.certificado.home.title' },
        loadChildren: () => import('./certificado/certificado.module').then(m => m.CertificadoModule),
      },
      {
        path: 'turma-acc',
        data: { pageTitle: 'controleDeAccApp.turmaACC.home.title' },
        loadChildren: () => import('./turma-acc/turma-acc.module').then(m => m.TurmaACCModule),
      },
      {
        path: 'curso',
        data: { pageTitle: 'controleDeAccApp.curso.home.title' },
        loadChildren: () => import('./curso/curso.module').then(m => m.CursoModule),
      },
      {
        path: 'tipo-atividade',
        data: { pageTitle: 'controleDeAccApp.tipoAtividade.home.title' },
        loadChildren: () => import('./tipo-atividade/tipo-atividade.module').then(m => m.TipoAtividadeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
