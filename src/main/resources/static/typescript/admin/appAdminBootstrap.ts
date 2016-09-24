/// <reference path="../../typings/browser/ambient/es6-shim/index.d.ts" />" />
/// <reference path="../../node_modules/zone.js/dist/zone.js.d.ts" />

import {bootstrapStatic} from '@angular/platform-browser';
import {provide} from '@angular/core';
import {HTTP_PROVIDERS} from '@angular/http';
import {AppAdminComponent} from './appAdminComponent';
import {ROUTER_PROVIDERS} from '@angular/router-deprecated';
import {COMPILER_PROVIDERS, XHR} from '@angular/compiler';
import {HashLocationStrategy} from '@angular/common/src/location/hash_location_strategy';
import {LocationStrategy} from '@angular/common/src/location/location_strategy';
import {HttpClient} from './util/httpClient';

bootstrapStatic(AppAdminComponent, [
	ROUTER_PROVIDERS,
	COMPILER_PROVIDERS,
	HTTP_PROVIDERS,
	provide(HttpClient, {useClass: HttpClient}),
	provide(XHR, {useClass: XHR}),
	provide(LocationStrategy, {useClass: HashLocationStrategy})
]);
