import {TestBed, inject} from '@angular/core/testing';

import {UserService} from './user.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ConfigService} from "./config.service";
import {User} from "../_models/user";

describe('UserService', () => {

  let userService: UserService;
  let httpMock: HttpTestingController;
  let configService: ConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserService, ConfigService],
      imports: [HttpClientTestingModule]
    });

    httpMock = TestBed.get(HttpTestingController);
    userService = TestBed.get(UserService);
    configService = TestBed.get(ConfigService);
  });

  it('create should pass and return correct user', () => {
    const testUser: User = {username: "testName", password: "testPassword"};

    userService.create(testUser).subscribe(user => {
      expect(user).toEqual(jasmine.objectContaining(testUser));
    });

    const req = httpMock.expectOne(configService.getBackUrl() + '/rest/user/register');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(testUser);
    req.flush(testUser);

    httpMock.verify();
  });

});
