.PHONY: clean

presentation.pdf: README.md
	pandoc --slide-level 3 --toc -t beamer README.md -o presentation.pdf

clean:
	$(RM) presentation.pdf
