import React, { useEffect, useState } from "react";
import Sketch from "react-p5";

const GraphCoil = (props) => {
  const [cuttingPatterns, setCuttingPatterns] = useState([[]]);
  const [calc, setCalc] = useState();

  useEffect(() => {
    if (props.requested) {
      const { calc } = props;
      setCalc(calc);
      const width = JSON.parse(calc.width);
      let processedPatterns = [];

      calc.cuttingPatterns.forEach((cuttingPattern) => {
        let processedPattern = [];
        let pattern = JSON.parse(cuttingPattern.pattern);

        pattern.forEach((itemQuantity, index) => {
          for (let i = 0; i < itemQuantity; i++) {
            processedPattern.push(width[index]);
          }
        });

        processedPatterns.push(processedPattern);
      });

      setCuttingPatterns(processedPatterns);
    }
  }, [props.requested]);

  useEffect(() => {
    p5 && p5.loop();
  }, [cuttingPatterns]);

  const [greenColor] = useState("rgb(189,236,182)");
  const [redColor] = useState("rgb(255,86,77)");

  let p5;

  const setup = (p, canvasParentRef) => {
    p5 = p;
    p.createCanvas(800, 1000, p.P2D).parent(canvasParentRef);
    p.noLoop();
  };

  const draw = (p) => {
    p.background(255);
    const spacing = 100; // Set your desired spacing between each row
    let yPos = 50; // Initial y position
    cuttingPatterns.forEach((pattern, index) => {
      drawCutPattern(
        pattern,
        calc?.cuttingPatterns[index].drawn,
        calc?.machineMaxWidth,
        yPos,
        p
      );
      yPos += spacing; // Increase y position after each pattern
    });
  };

  const drawCutPattern = (pattern, drawnCount, machineLength, yPos, p) => {
    let scale = p.width / machineLength;
    let xPos = 0;

    p.fill(0);
    p.textAlign(p.LEFT, p.CENTER);
    p.text("Tiradas: " + drawnCount, 0, yPos - 20);

    for (let i = 0; i < pattern.length; i++) {
      p.fill(p.color(greenColor));
      p.rect(xPos, yPos, pattern[i] * scale, 50);
      p.fill(0);
      p.textAlign(p.CENTER, p.CENTER);
      p.text(pattern[i], xPos + (pattern[i] * scale) / 2, yPos + 25);
      xPos += pattern[i] * scale;
    }

    let waste = machineLength - sum(pattern);

    if (waste > 0) {
      p.fill(p.color(redColor));
      p.rect(xPos, yPos, waste * scale, 50);
      p.fill(0);
      p.textAlign(p.CENTER, p.CENTER);
      p.text(waste, xPos + (waste * scale) / 2, yPos + 25);
    }
  };

  const sum = (arr) => arr.reduce((a, b) => a + b, 0);

  return <Sketch setup={setup} draw={draw} />;
};

export default GraphCoil;
