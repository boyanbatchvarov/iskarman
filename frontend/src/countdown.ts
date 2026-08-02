interface CountdownParts {
  months: number;
  days: number;
  hours: number;
  minutes: number;
  seconds: number;
}

interface CountdownLabels {
  months: string;
  days: string;
  hours: string;
  minutes: string;
  seconds: string;
}

function compute(from: number, to: number): CountdownParts {
  if (to <= from) {
    return { months: 0, days: 0, hours: 0, minutes: 0, seconds: 0 };
  }

  let cursor = new Date(from);
  const end = new Date(to);
  let months = 0;

  while (true) {
    const next = new Date(cursor);
    next.setMonth(next.getMonth() + 1);
    if (next > end) break;
    months++;
    cursor = next;
  }

  let days = 0;
  while (true) {
    const next = new Date(cursor);
    next.setDate(next.getDate() + 1);
    if (next > end) break;
    days++;
    cursor = next;
  }

  const remainingMs = end.getTime() - cursor.getTime();
  const hours = Math.floor(remainingMs / (1000 * 60 * 60));
  const minutes = Math.floor((remainingMs % (1000 * 60 * 60)) / (1000 * 60));
  const seconds = Math.floor((remainingMs % (1000 * 60)) / 1000);

  return { months, days, hours, minutes, seconds };
}

function initCountdown(countdownEl: HTMLElement): void {
  const targetMs = Number(countdownEl.dataset.target);
  if (!Number.isFinite(targetMs)) return;

  const labels: CountdownLabels = {
    months: countdownEl.dataset.labelMonths || "months",
    days: countdownEl.dataset.labelDays || "days",
    hours: countdownEl.dataset.labelHours || "hours",
    minutes: countdownEl.dataset.labelMinutes || "minutes",
    seconds: countdownEl.dataset.labelSeconds || "seconds",
  };

  function render(): void {
    const { months, days, hours, minutes, seconds } = compute(Date.now(), targetMs);
    countdownEl.innerHTML =
      '<span class="countdown-value">' + months + "</span>" +
      '<span class="countdown-unit">' + labels.months + "</span>" +
      '<span class="countdown-sep">:</span>' +
      '<span class="countdown-value">' + days + "</span>" +
      '<span class="countdown-unit">' + labels.days + "</span>" +
      '<span class="countdown-sep">:</span>' +
      '<span class="countdown-value">' + hours + "</span>" +
      '<span class="countdown-unit">' + labels.hours + "</span>" +
      '<span class="countdown-sep">:</span>' +
      '<span class="countdown-value">' + minutes + "</span>" +
      '<span class="countdown-unit">' + labels.minutes + "</span>" +
      '<span class="countdown-sep">:</span>' +
      '<span class="countdown-value">' + seconds + "</span>" +
      '<span class="countdown-unit">' + labels.seconds + "</span>";
  }

  render();
  setInterval(render, 1_000);
}

const countdownEl = document.getElementById("countdown");
if (countdownEl) {
  initCountdown(countdownEl);
}
