/// <reference path="../../../typings/main/ambient/jasmine/index.d.ts" />

import {it, describe, expect, beforeEach, inject, beforeEachProviders} from 'angular2/testing';
import {AdminHeaderElementsProvider} from './adminHeaderElementsProvider';
import {provide} from 'angular2/core';

let adminHeaderElementsProvider: AdminHeaderElementsProvider;

beforeEach(() => {
	adminHeaderElementsProvider = new AdminHeaderElementsProvider();
});

describe('Service: AdminHeaderElementsProvider', () => {
	it('should have two elements', () => {
		expect(adminHeaderElementsProvider.getElements().length).toBe(2);
	});
});
