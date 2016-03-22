module.exports = function(grunt) {
	grunt.config.merge({
		watch: {
			sass: {
				files: 'sass/**/*.sass',
				tasks: 'sass'
			},
			ts: {
				files: 'typescript/**/*.ts',
				tasks: 'ts'
			}
		}
	});

	grunt.loadNpmTasks('grunt-contrib-watch');
};
