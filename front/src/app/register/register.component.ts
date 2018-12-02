import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {UserService} from '../_services/user.service';
import {User} from '../_models/user';
import {AlertService} from '../_services/alert.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(private router: Router,
              private registerService: UserService,
              private alertService: AlertService) {
  }

  model: User;
  loading = false;

  ngOnInit() {
    this.model = new User(null, null);
  }

  register() {
    this.registerService.create(this.model).subscribe(
      onSuccess => {
        this.router.navigate(['/login']);
      },
      error => {
        this.alertService.error('Something goes wrong');
      }
    );
  }


}
