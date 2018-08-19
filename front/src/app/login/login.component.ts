import {Component, OnInit} from '@angular/core';
import {AuthService} from "angular5-social-login";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private socialAuthService: AuthService) {
  }

  ngOnInit() {
  }

  public socialSignIn(socialPlatform: string) {
    this.socialAuthService.signIn(socialPlatform).then(
      (userData) => {
        console.log(userData); //TODO: send to backend
      }
    );
  }
}
