let currentIndex = /*[[${currentIndex}]]*/ 0;
let examDetails = [[${examDetailList}]] [];

if (!Array.isArray(examDetails)) {
    examDetails = [];
}

function showQuestion(index) {
    const questions = document.querySelectorAll('.question');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const submitBtn = document.getElementById('submitBtn');

    questions.forEach((question, idx) => {
        question.style.display = idx === index ? 'block' : 'none';
    });

    prevBtn.style.display = index === 0 ? 'none' : 'inline-block';
    nextBtn.style.display = index === questions.length - 1 ? 'none' : 'inline-block';
    submitBtn.style.display = index === questions.length - 1 ? 'inline-block' : 'none';

    renderMathInElement(document.body, {
        delimiters: [
            {left: '$$', right: '$$', display: true},
            {left: '$', right: '$', display: false},
            {left: "\\(", right: "\\)", display: false},
            {left: "\\begin{equation}", right: "\\end{equation}", display: true},
            {left: "\\begin{align}", right: "\\end{align}", display: true},
            {left: "\\begin{alignat}", right: "\\end{alignat}", display: true},
            {left: "\\begin{gather}", right: "\\end{gather}", display: true},
            {left: "\\begin{CD}", right: "\\end{CD}", display: true},
            {left: "\\begin{tikzpicture}", right: "\\end{tikzpicture}", display: true},
            {left: "\\[", right: "\\]", display: true}
        ],
        ignoredClasses: ["disable-katex-render"],
        throwOnError: false
    });
}

function changeQuestion(direction) {
    const questions = document.querySelectorAll('.question');
    currentIndex += direction;
    if (currentIndex < 0) currentIndex = 0;
    if (currentIndex >= questions.length) currentIndex = questions.length - 1;

    showQuestion(currentIndex);
}

function updateSelectedOption(examDetail, quizOption) {
    if (quizOption) {
        examDetails.forEach(detail => {
            if (detail.id === examDetail.id) {
                detail.selectedOption = quizOption;
                console.log(detail); // In ra detail để kiểm tra
            }
        });
    } else {
        console.error(`QuizOption not found.`);
    }
}

function submitExam() {
    fetch('/exams/submitExam', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(examDetails)
    })
        .then(response => response.text())
        .catch(error => {
            console.error('Error submitting exam:', error);
        });
}

document.addEventListener('DOMContentLoaded', function () {
    showQuestion(currentIndex);
});