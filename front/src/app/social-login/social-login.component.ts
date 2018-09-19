import {Component, OnInit} from '@angular/core';
import {AuthService, SocialUser} from "angular5-social-login";
import {AlertService} from "../_services/alert.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../_services/authentication.service";
import {TokenService} from "../_services/token.service";

@Component({
  selector: 'social-login',
  templateUrl: './social-login.component.html',
  styleUrls: ['./social-login.component.css']
})
export class SocialLoginComponent implements OnInit {

  constructor(
    private socialAuthService: AuthService,
    private router: Router,
    private tokenService: TokenService,
    private alertService: AlertService,
    private route: ActivatedRoute,
    private authenticationService: AuthenticationService
  ) {
  }

  private returnUrl: string;

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  public socialSignIn(socialPlatform: string) {
    this.socialAuthService.signIn(socialPlatform).then((data) => {
      this.login(data)
    });
  }

  private login(userData: SocialUser) {
    this.authenticationService.socialLogin(userData.provider, userData.token).subscribe(
      token => {
        this.tokenService.saveToken(token);
        this.router.navigate([this.returnUrl]);
      },
      error => {
        console.log(error);
        this.alertService.error(error.error);
      });
  }
}
