import {Component, OnInit} from '@angular/core';
import {AuthService} from 'angularx-social-login';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../_services/authentication.service';
import {TokenService} from '../_services/token.service';
import {AlertService} from '../_services/alert.service';
import {SocialLoginComponent} from '../social-login/social-login.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(
    private socialAuthService: AuthService,
    private router: Router,
    private tokenService: TokenService,
    private alertService: AlertService,
    private route: ActivatedRoute,
    private authenticationService: AuthenticationService
  ) {
  }

  private socialLoginComponent: SocialLoginComponent;
  private returnUrl: string;

  ngOnInit() {
    this.authenticationService.logout();
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }
}
