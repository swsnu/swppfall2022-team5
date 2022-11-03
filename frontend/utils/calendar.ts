export const getWeeksInMonth = (year: number, month: number) => {
  let weeks = [],
    firstDate = new Date(year, month, 1),
    lastDate = new Date(year, month + 1, 0),
    numDays = lastDate.getDate();

  let start = 1;
  let end;
  if (firstDate.getDay() === 1) {
    end = 7;
  } else if (firstDate.getDay() === 0) {
    let preMonthEndDay = new Date(year, month, 0);
    start = preMonthEndDay.getDate() - 6 + 1;
    end = 1;
  } else {
    let preMonthEndDay = new Date(year, month, 0);
    start = preMonthEndDay.getDate() + 1 - firstDate.getDay() + 1;
    end = 7 - firstDate.getDay() + 1;
    weeks.push({
      start: start,
      end: end,
    });
    start = end + 1;
    end = end + 7;
  }
  while (start <= numDays) {
    weeks.push({
      start: start,
      end: end,
    });
    start = end + 1;
    end = end + 7;
    end = start === 1 && end === 8 ? 1 : end;
    if (end > numDays && start <= numDays) {
      end = end - numDays;
      weeks.push({
        start: start,
        end: end,
      });
      break;
    }
  }

  return weeks.map(({ start, end }, index) => {
    const sub = +(start > end && index === 0);
    return Array.from({ length: 7 }, (_, index) => {
      const date = new Date(year, month - sub, start + index);
      return date;
    });
  });
};

export const getWeekContainingDate = (target: Date): Date[] => {
  const month = getWeeksInMonth(target.getFullYear(), target.getMonth());
  for (const week of month) {
    for (const day of week) {
      if (isSameDate(day, target)) {
        return week;
      }
    }
  }
  return month[0]; // shoudn't happen
};

export const isSameDate = (date1: Date, date2: Date): boolean => {
  return date1.toDateString() === date2?.toDateString();
};
