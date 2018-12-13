import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {UserService} from '../_services/user.service';
import {User} from '../_models/user';
import {AlertService} from '../_services/alert.service';
import {sha512} from "js-sha512";

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
    this.registerService.create(new User(this.model.username, sha512(this.model.password))).subscribe(
      onSuccess => {
        this.router.navigate(['/login']);
      },
      error => {
        this.alertService.error(error.error);
      }
    );
  }


}
