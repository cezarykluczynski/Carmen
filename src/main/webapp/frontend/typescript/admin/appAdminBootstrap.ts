/// <reference path="../../node_modules/angular2/typings/browser.d.ts" />
/// <reference path="../../node_modules/zone.js/dist/zone.js.d.ts" />

import {bootstrap} from 'angular2/platform/browser';
import {provide} from 'angular2/core';
import {AppAdminComponent} from './appAdminComponent';
import {ROUTER_PROVIDERS, LocationStrategy, HashLocationStrategy} from 'angular2/router';

bootstrap(AppAdminComponent, [
	ROUTER_PROVIDERS,
	provide(LocationStrategy, {useClass: HashLocationStrategy})
]);
