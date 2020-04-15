export function typeColor(type) {
  if ("BUSINESSMAN" === type) {
    return "light-blue";
  } else if ("GENERAL" === type) {
    return "deep-orange";
  } else if ("KRYSTALIUM" === type) {
    return "red";
  } else if ("MATERIAL" === type) {
    return "grey";
  } else if ("ENERGY" === type) {
    return "black";
  } else if ("SCIENCE" === type) {
    return "green";
  } else if ("GOLD" === type) {
    return "amber";
  } else if ("DISCOVERY" === type) {
    return "indigo";
  }

  return null;
}
