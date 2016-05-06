import {it, describe, expect} from '@angular/core/testing';
import {FormatterService} from './formatterService';

describe('Service: FormatterService', () => {
	describe('booleanToWord', () => {
		it('formatts boolean when it is not defined', () => {
			expect(FormatterService.booleanToWord(undefined)).toBe('unknown');
			expect(FormatterService.booleanToWord(null)).toBe('unknown');
		});

		it('formatts boolean when it is defined', () => {
			expect(FormatterService.booleanToWord(true)).toBe('yes');
			expect(FormatterService.booleanToWord(false)).toBe('no');
		});
	});

	describe('formatGenericOptional', () => {
		it('says "unknown" for undefined or null values', () => {
			expect(FormatterService.formatGenericOptional(undefined)).toBe('unknown');
			expect(FormatterService.formatGenericOptional(null)).toBe('unknown');
		});

		it('formats provided values', () => {
			expect(FormatterService.formatGenericOptional('test')).toBe('test');
			expect(FormatterService.formatGenericOptional(5)).toBe('5');
		});
	});
});
