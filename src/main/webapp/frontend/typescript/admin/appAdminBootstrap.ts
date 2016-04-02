/// <reference path="../../node_modules/angular2/typings/browser.d.ts" />
/// <reference path="../../node_modules/zone.js/dist/zone.js.d.ts" />

import {bootstrap} from 'angular2/platform/browser';
import {provide} from 'angular2/core';
import {HTTP_PROVIDERS} from 'angular2/http';
import {AppAdminComponent} from './appAdminComponent';
import {ROUTER_PROVIDERS, LocationStrategy, HashLocationStrategy} from 'angular2/router';
import {HttpClient} from './util/httpClient';

bootstrap(AppAdminComponent, [
	ROUTER_PROVIDERS,
	HTTP_PROVIDERS,
	provide(HttpClient, {useClass: HttpClient}),
	provide(LocationStrategy, {useClass: HashLocationStrategy})
]);
