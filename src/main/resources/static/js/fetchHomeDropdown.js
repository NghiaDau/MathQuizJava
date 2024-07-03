$(document).ready(function() {
    // Load grades when a level button is clicked
    $(document).on('click', '.level-button', function() {
        var levelId = $(this).data('level-id');
        $('#gradeButtons').empty().prop('disabled', false);
        $('#mathTypeButtons').empty().prop('disabled', true).hide();
        $('#chapterButtons').empty().prop('disabled', true).hide();
        $('#quizMatrixList').empty();

        // Highlight selected level button
        $('.level-button').removeClass('btn-primary').addClass('btn-outline-primary');
        $(this).removeClass('btn-outline-primary').addClass('btn-primary');

        if (levelId) {
            $.get("/getGradesByLevel", { levelId: levelId }, function(data) {
                data.forEach(function(grade) {
                    $('#gradeButtons').append('<button class="btn btn-outline-primary grade-button" data-grade-id="' + grade.id + '">' + grade.name + '</button>');
                });
            });
        }
    });

    // Load math types when a grade button is clicked
    $(document).on('click', '.grade-button', function() {
        var gradeId = $(this).data('grade-id');
        $('#mathTypeButtons').empty().prop('disabled', false).show();
        $('#chapterButtons').empty().prop('disabled', true).hide();
        $('#quizMatrixList').empty();

        // Highlight selected grade button
        $('.grade-button').removeClass('btn-primary').addClass('btn-outline-primary');
        $(this).removeClass('btn-outline-primary').addClass('btn-primary');

        if (gradeId) {
            $.get("/getMathTypesByGrade", { gradeId: gradeId }, function(data) {
                data.forEach(function(mathType) {
                    $('#mathTypeButtons').append('<button class="btn btn-outline-primary math-type-button" data-math-type-id="' + mathType.id + '">' + mathType.name + '</button>');
                });
            });
        }
    });

    // Load chapters when a math type button is clicked
    $(document).on('click', '.math-type-button', function() {
        var gradeId = $('.grade-button.btn-primary').data('grade-id');
        var mathTypeId = $(this).data('math-type-id');
        $('#chapterButtons').empty().prop('disabled', false).show();
        $('#quizMatrixList').empty();

        // Highlight selected math type button
        $('.math-type-button').removeClass('btn-primary').addClass('btn-outline-primary');
        $(this).removeClass('btn-outline-primary').addClass('btn-primary');

        if (gradeId && mathTypeId) {
            $.get("/getChaptersByGradeAndMathType", { gradeId: gradeId, mathTypeId: mathTypeId }, function(data) {
                data.forEach(function(chapter) {
                    $('#chapterButtons').append('<button class="btn btn-outline-primary chapter-button" data-chapter-id="' + chapter.id + '">' + chapter.name + '</button>');
                });
            });
        }
    });

    // Load quiz matrices when a chapter button is clicked
    $(document).on('click', '.chapter-button', function() {
        var chapterId = $(this).data('chapter-id');
        $('#quizMatrixList').empty();

        // Highlight selected chapter button
        $('.chapter-button').removeClass('btn-primary').addClass('btn-outline-primary');
        $(this).removeClass('btn-outline-primary').addClass('btn-primary');

        if (chapterId) {
            $.get("/getQuizMatricesByChapter", { chapterId: chapterId }, function(data) {
                data.forEach(function(quizMatrix) {
                    var listItem = $('<li>');
                    var link = $('<a>')
                        .attr('href', '#')
                        .text(quizMatrix.name)
                        .appendTo(listItem);

                    listItem.appendTo($('#quizMatrixList'));
                });
            });
        }
    });
});
