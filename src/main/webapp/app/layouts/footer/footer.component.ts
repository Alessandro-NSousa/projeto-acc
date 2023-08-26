import { Component } from '@angular/core';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import { faGithub, faInstagram, faLinkedin } from '@fortawesome/free-brands-svg-icons';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent {
  faHome = faHome;
  github = faGithub;
  linkedin = faLinkedin;
  instagram = faInstagram;
}
