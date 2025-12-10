import numpy as np
from scipy.optimize import milp, LinearConstraint, Bounds

answer = 0

with open("Day10.txt", "r") as file:
    for line in file:
        parts = line.strip().split(' ')

        buttons = [[int(it) for it in part.strip('()').split(',')] for part in parts[1:-1]]
        counters = [int(it) for it in parts[-1].strip('{}').split(',')]

        M = len(counters)
        N = len(buttons)

        # Each button sparsely encodes a vector of size M, where the numbers
        # correspond to indices of ones (all other entries are zeroes).
        A = np.zeros((M, N), dtype=int)
        for j, idx in enumerate(buttons):
            A[idx, j] = 1

        # Joltage counter requirements are the target vector.
        y = np.array(counters)

        # We're effectively solving the problem A @ x = y. However, scipy uses
        # a more general form with lower and upper bounds: lb <= A @ x <= ub.
        constraint = LinearConstraint(A, lb=y, ub=y)

        # All decision variables (i.e., button presses) are integers, since
        # we can only press each button an integer number of times.
        integrality = np.ones(N)

        # All decision variables are in the range [0, ∞). In other words, we
        # cannot do a negative amount of button presses.
        bounds = Bounds(0, np.inf)

        # The objective function we're trying to minimize is sum(x), or the
        # total number of button presses, so we don't care about coefficients.
        c = np.ones(N)

        result = milp(c=c, constraints=constraint, bounds=bounds, integrality=integrality)
        answer += result.x.sum()

print(int(answer))
