# Use an official Python runtime as a parent image
FROM python:3.9-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Install dependencies for psycopg2 and other packages
RUN apt-get update && apt-get install -y libpq-dev

# Install Python packages from requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Set FLASK_APP to the Python script name
ENV FLASK_APP=technical_analysis.py

# Make port 5003 available to the world outside this container
EXPOSE 5003

# Run the Flask app
CMD ["flask", "run", "--host=0.0.0.0", "--port=5003"]
