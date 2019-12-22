$('.datepicker').daterangepicker({
    "timePicker": false,
    "minDate": new Date(),
    "maxSpan": {
        "days": 14
    },
    "drops": "up",
    "locale": {
//        "format": "DD/MM/YYYY",
        "format": "YYYY/MM/DD",
        "separator": " - ",
        "applyLabel": "Potwierdź",
        "cancelLabel": "Anuluj",
        "fromLabel": "Od",
        "toLabel": "Do",
        "customRangeLabel": "Własny zakres",
        "weekLabel": "T",
        "daysOfWeek": [
            "Nie",
            "Pon",
            "Wto",
            "Śro",
            "Czw",
            "Pią",
            "Sob"
        ],
        "monthNames": [
            "Styczeń",
            "Luty",
            "Marzec",
            "Kwiecień",
            "Maj",
            "Czerwiec",
            "Lipiec",
            "Sierpień",
            "Wrzesień",
            "Październik",
            "Listopad",
            "Grudzień"
        ],
        "firstDay": 1


    },
    isInvalidDate: function (date) {
        var dateRanges = [
            {'start': moment('2019-12-25'), 'end': moment('2019-12-26')},
            {'start': moment('2020-01-01'), 'end': moment('2020-01-01')},
            {'start': moment('2020-06-01'), 'end': moment('2020-06-01')},
            {'start': moment('2020-05-03'), 'end': moment('2020-05-03')},
            {'start': moment('2020-08-15'), 'end': moment('2020-08-15')},
            {'start': moment('2020-11-01'), 'end': moment('2020-11-01')},
            {'start': moment('2020-11-11'), 'end': moment('2020-11-11')},
            {'start': moment('2020-12-25'), 'end': moment('2020-12-26')},
            {'start': moment('2020-04-12'), 'end': moment('2020-04-13')},
            {'start': moment('2020-05-31'), 'end': moment('2020-05-31')},
            {'start': moment('2020-06-11'), 'end': moment('2020-06-11')}
        ];
        return dateRanges.reduce(function (bool, range) {
            return bool || (date >= range.start && date <= range.end);
        }, false);
    }
});