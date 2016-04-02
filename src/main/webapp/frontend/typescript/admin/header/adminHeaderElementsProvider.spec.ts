/// <reference path="../../../typings/main/ambient/jasmine/index.d.ts" />

import {it, describe, expect, beforeEach} from 'angular2/testing';
import {AdminHeaderElementsProvider} from './adminHeaderElementsProvider';

describe('Service: AdminHeaderElementsProvider', () => {
	let adminHeaderElementsProvider: AdminHeaderElementsProvider;

	beforeEach(() => {
		adminHeaderElementsProvider = new AdminHeaderElementsProvider();
	});

	it('should have two elements', () => {
		expect(adminHeaderElementsProvider.getElements().length).toBe(2);
	});
});
