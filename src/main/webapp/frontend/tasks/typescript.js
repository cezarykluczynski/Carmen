module.exports = function(grunt) {
	grunt.config.merge({
		ts: {
			options: {
				experimentalDecorators: true,
				target: "es5",
				module: "system",
				moduleResolution: "node",
				sourceMap: true,
				emitDecoratorMetadata: true,
				removeComments: false,
				noImplicitAny: false,
				fast: 'never'
			},
			default : {
				files: [
					{
						src: ['typescript/**/*.ts'],
						dest: 'dist/javascript'
					}
				]
			}
		}
	});

	grunt.loadNpmTasks("grunt-ts");
};
